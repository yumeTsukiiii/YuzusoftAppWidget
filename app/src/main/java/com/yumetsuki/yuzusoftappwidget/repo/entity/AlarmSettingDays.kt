package com.yumetsuki.yuzusoftappwidget.repo.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "alarm_setting_days",
    foreignKeys = [
        ForeignKey(
            entity = AlarmSetting::class,
            childColumns = ["setting_id"],
            parentColumns = ["id"]
        )
    ]
)
@Parcelize
class AlarmSettingDays(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "setting_id")
    val settingId: Int
): Parcelable