package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo

class NoIdAlarmSettingDays(

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "setting_id")
    val settingId: Int
)