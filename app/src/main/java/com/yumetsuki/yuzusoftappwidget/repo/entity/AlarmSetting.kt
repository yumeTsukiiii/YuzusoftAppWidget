package com.yumetsuki.yuzusoftappwidget.repo.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "alarm_setting")
@Parcelize
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

    @ColumnInfo(name = "enable")
    val isEnable: Boolean
): Parcelable