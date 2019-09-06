package com.yumetsuki.yuzusoftappwidget.utils

import com.yumetsuki.yuzusoftappwidget.model.NoIdAlarmSetting
import com.yumetsuki.yuzusoftappwidget.model.NoIdAlarmSettingDays
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays

fun AlarmSetting.toNoId() = NoIdAlarmSetting(icon, alarmHour, alarmMinute, isEnable)

fun AlarmSettingDays.toNoId() = NoIdAlarmSettingDays(day, settingId)