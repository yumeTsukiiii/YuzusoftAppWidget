package com.yumetsuki.yuzusoftappwidget.page.alarm_settings.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.AlarmTaskInterface
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter.AlarmSettingsAdapter
import com.yumetsuki.yuzusoftappwidget.repo.AlarmRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmSettingsViewModel(
    application: Application
): AndroidViewModel(application) {

    val newAlarmId = -10000

    val alarmSettings = MutableLiveData<ArrayList<AlarmSettingModel>>()

    var alarmSettingsAdapter: AlarmSettingsAdapter? = null

    private val alarmRepository = AlarmRepository.create(application)

    private val serviceConnection by lazy {
        object :ServiceConnection {

            override fun onServiceDisconnected(name: ComponentName) {

            }

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                alarmService = AlarmTaskInterface.Stub.asInterface(service)
            }

        }
    }

    private lateinit var alarmService: AlarmTaskInterface

    init {
        viewModelScope.launch(Dispatchers.IO) {
            alarmSettings.postValue(alarmRepository.getAllAlarms() as ArrayList<AlarmSettingModel>)
        }

        getApplication<Application>().bindService(Intent(
            "com.yumetsuki.yuzusoftappwidget.service.TimeAlarmService"
        ).apply {
            `package` = "com.yumetsuki.yuzusoftappwidget"
        }, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun newAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepository.insertAlarm(alarmSettingModel)
        alarmSettings.value?.apply {
            add(alarmSettingModel)
            alarmSettings.postValue(alarmRepository.getAllAlarms().apply {
                alarmService.newAlarm(last().alarmSetting.id)
            } as ArrayList<AlarmSettingModel>)
        }
    }

    fun deleteAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch(Dispatchers.IO) {
        alarmService.removeAlarm(alarmSettingModel.alarmSetting.id)
        alarmRepository.deleteAlarm(alarmSettingModel)
        alarmSettings.value?.apply {
            removeAll { it.alarmSetting.id == alarmSettingModel.alarmSetting.id }
            alarmSettings.postValue(this)
        }
    }

    fun toggleAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch(Dispatchers.IO) {
        alarmService.updateAlarm(alarmSettingModel.alarmSetting.id)
        alarmRepository.toggleAlarm(alarmSettingModel)
        alarmSettings.value?.apply {
            this[
                    indexOfFirst {
                        it.alarmSetting.id == alarmSettingModel.alarmSetting.id
                    }
            ] = AlarmSettingModel(
                AlarmSetting(
                    alarmSettingModel.alarmSetting.id,
                    alarmSettingModel.alarmSetting.icon,
                    alarmSettingModel.alarmSetting.alarmHour,
                    alarmSettingModel.alarmSetting.alarmMinute,
                    !alarmSettingModel.alarmSetting.isEnable
                ),
                alarmSettingModel.days
            )
            alarmSettings.postValue(this)
        }
    }

    fun updateAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepository.updateAlarm(
            alarmSettings.value!!.find { it.alarmSetting.id == alarmSettingModel.alarmSetting.id }!!,
            alarmSettingModel
        )
        alarmSettings.value?.apply {
            val index = indexOfFirst { it.alarmSetting.id == alarmSettingModel.alarmSetting.id }
            this[index] = alarmSettingModel
            alarmSettings.postValue(
                alarmRepository.getAllAlarms() as ArrayList<AlarmSettingModel>
            )
            alarmService.updateAlarm(alarmSettingModel.alarmSetting.id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unbindService(serviceConnection)
    }

}