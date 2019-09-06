package com.yumetsuki.yuzusoftappwidget.broadcast

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.app_widget.YuzusoftTimeRemindWidgetProvider
import java.util.Calendar

/**
 * 该广播用于监听时刻变化
 * */
class MinutesTimerBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when(intent.action) {

            Intent.ACTION_TIME_TICK -> {
                //发送更新时间广播

                if (!Status.isScreenOff) {

                    context.sendBroadcast(Intent(YuzusoftTimeRemindWidgetProvider.UPDATE_ACTION).apply {
                        component = ComponentName(
                            context,
                            YuzusoftTimeRemindWidgetProvider::class.java
                        )
                    })

                }

            }

            //亮屏
            Intent.ACTION_SCREEN_ON -> {
                Status.isScreenOff = false
                context.sendBroadcast(Intent(YuzusoftTimeRemindWidgetProvider.UPDATE_ACTION).apply {
                    component = ComponentName(
                        context,
                        YuzusoftTimeRemindWidgetProvider::class.java
                    )
                })
            }
            //熄屏
            Intent.ACTION_SCREEN_OFF -> {
                Status.isScreenOff = true
            }

        }

    }


}