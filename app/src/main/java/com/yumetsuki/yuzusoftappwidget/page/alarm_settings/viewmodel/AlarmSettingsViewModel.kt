package com.yumetsuki.yuzusoftappwidget.page.alarm_settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter.AlarmSettingsAdapter
import com.yumetsuki.yuzusoftappwidget.repo.AlarmRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AlarmSettingsViewModel(
    application: Application
): AndroidViewModel(application) {

    val alarmSettings = MutableLiveData<ArrayList<AlarmSettingModel>>()

    var alarmSettingsAdapter: AlarmSettingsAdapter? = null

    private val alarmRepository = AlarmRepository.create(application)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            alarmSettings.postValue(alarmRepository.getAllAlarms() as ArrayList<AlarmSettingModel>)
        }
    }

    fun newAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch(Dispatchers.IO) {
        alarmRepository.insertAlarm(alarmSettingModel)
        alarmSettings.value?.apply {
            add(alarmSettingModel)
            alarmSettings.postValue(this)
        }
    }

    fun deleteAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch {
        alarmRepository.deleteAlarm(alarmSettingModel)
        alarmSettings.value?.apply {
            removeAll { it.alarmSetting.id == alarmSettingModel.alarmSetting.id }
            alarmSettings.postValue(this)
        }
    }

    fun toggleAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch {
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
                    alarmSettingModel.alarmSetting.voiceResource,
                    !alarmSettingModel.alarmSetting.isEnable
                ),
                alarmSettingModel.days
            )
            alarmSettings.postValue(this)
        }
    }

    fun updateAlarm(alarmSettingModel: AlarmSettingModel) = viewModelScope.launch {
        alarmRepository.updateAlarm(
            alarmSettings.value!!.find { it.alarmSetting.id == alarmSettingModel.alarmSetting.id }!!,
            alarmSettingModel
        )
    }

}