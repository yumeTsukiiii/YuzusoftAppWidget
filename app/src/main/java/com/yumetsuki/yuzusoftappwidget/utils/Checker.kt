package com.yumetsuki.yuzusoftappwidget.utils

import android.app.ActivityManager
import android.content.Context

/**
 * 检查闹钟服务是否正在运行中
 * */
fun Context.checkAlarmServiceRunning(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.runningAppProcesses.any {
        it.processName == "com.yumetsuki.yuzusoftappwidget:remoteTimeAlarm"
    }
}