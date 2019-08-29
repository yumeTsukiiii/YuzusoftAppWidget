package com.yumetsuki.yuzusoftappwidget

import com.yumetsuki.yuzusoftappwidget.utils.pref

object Status {

    var isCharacterPlaying by pref(false)

    var isStartTimeReminder by pref(false)

    var isScreenOff by pref(false)

}