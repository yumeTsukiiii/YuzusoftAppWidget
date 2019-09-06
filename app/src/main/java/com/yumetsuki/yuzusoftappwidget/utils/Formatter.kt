package com.yumetsuki.yuzusoftappwidget.utils

import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel

fun AlarmSettingModel.formatTimeText(): String {
    return alarmSetting.run {
        "${
        (if (alarmHour > 12) {
            alarmHour - 12
        } else {
            alarmHour
        }).let { 
            if (it < 10) {
                "0"
            } else {
                ""
            } + it
        }
        }:${alarmMinute.let {
            if (it < 10) {
                "0"
            } else {
                ""
            } + it
        }} ${
        if (alarmHour > 12) {
            "pm"
        } else {
            "am"
        }
        }"
    }
}

fun AlarmSettingModel.formatDaysText(): String {
    return formatDaysText(this.days.map { it.day })
}

fun formatDaysText(days: List<Int>): String {
    return if (
        days.size == 2
        && days.containsAll(listOf(6, 7))
    ) {
        "双休"
    } else if (
        days.size == 7
    ) {
        "每天"
    } else {
        days.joinToString("、") {
            when(it) {
                1 -> {
                    "周一"
                }
                2 -> {
                    "周二"
                }
                3 -> {
                    "周三"
                }
                4 -> {
                    "周四"
                }
                5 -> {
                    "周五"
                }
                6 -> {
                    "周六"
                }
                7 -> {
                    "周日"
                }
                else -> {
                    ""
                }
            }
        }
    }
}