package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_setting")
class AlarmSetting(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "icon")
    val icon: Int,

    @ColumnInfo(name = "alarm_hour")
    val alarmHour: Int,

    @ColumnInfo(name = "alarm_minute")
    val alarmMinute: Int,

    @ColumnInfo(name = "voice_resource")
    val voiceResource: Int,

    @ColumnInfo(name = "enable")
    val isEnable: Boolean
)