package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo

data class NoIdAlarmSetting(
    @ColumnInfo(name = "icon")
    val icon: Int,

    @ColumnInfo(name = "alarm_hour")
    val alarmHour: Int,

    @ColumnInfo(name = "alarm_minute")
    val alarmMinute: Int,

    @ColumnInfo(name = "enable")
    val isEnable: Boolean
)