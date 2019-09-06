package com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.model.DateChosen
import com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify.adapter.DateRemindChooseAdapter

class AlarmSettingModifyViewModel: ViewModel() {

    var alarmSettingModel: AlarmSettingModel? = null

    val alarmHour = MutableLiveData<Int>()

    val alarmMinutes = MutableLiveData<Int>()

    val alarmIcon = MutableLiveData<Int>()

    val dateChosen = MutableLiveData<ArrayList<DateChosen>>()

    var dateRemindChooseAdapter: DateRemindChooseAdapter? = null

    fun changeDateChosenStatus(position: Int, isEnable: Boolean) {
        dateChosen.value?.apply {
            this[position].isEnable = isEnable
            dateChosen.postValue(this)
        }
    }

}