package com.yumetsuki.yuzusoftappwidget.model

import android.os.Parcelable
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlarmSettingModel(
    val alarmSetting: AlarmSetting,
    val days: List<AlarmSettingDays>
): Parcelable