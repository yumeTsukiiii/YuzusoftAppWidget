package com.yumetsuki.yuzusoftappwidget.app_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.service.TimeAlarmService
import com.yumetsuki.yuzusoftappwidget.service.TimeInsureService
import java.util.*

class YuzusoftTimeRemindWidgetProvider: AppWidgetProvider() {

    private lateinit var remoteViews: RemoteViews

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        //创建远程视图
        remoteViews = RemoteViews(context.packageName, R.layout.time_remind_widget_layout)

        //注册点击事件
        remoteViews.setOnClickPendingIntent(
            R.id.time_remind_controller,
            registerTimeRemindAction(context)
        )

        remoteViews.setOnClickPendingIntent(
            R.id.alarm_remind_controller,
            registerAlarmRemindAction(context)
        )

        remoteViews.setImageViewResource(
            R.id.time_remind_controller,
            if (Status.isStartTimeReminder) {
                R.drawable.ic_volume_up_pink_24dp
            } else {
                R.drawable.ic_volume_off_pink_24dp
            }
        )

        remoteViews.setImageViewResource(
            R.id.alarm_remind_controller,
            if (Status.isStartAlarmReminder) {
                R.drawable.ic_alarm_on_pink_24dp
            } else {
                R.drawable.ic_alarm_off_pink_24dp
            }
        )

        remoteViews.setTextViewText(R.id.time_text, formatTimeString())

        //对所有该应用的小部件进行刷新, 并保持一个实例

        for (appWidgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Status.isStartTimeReminder = false
        Status.isStartAlarmReminder = false
        context.stopService(
            Intent(
                context,
                TimeInsureService::class.java
            )
        )
        context.stopService(
            Intent(
                context,
                TimeAlarmService::class.java
            )
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            //点击开关报时
            TIME_REMIND_CLICK_ACTION -> {
                Status.isStartTimeReminder = !Status.isStartTimeReminder
                onTimeReminderToggle(context)
            }
            //更新时间数字
            UPDATE_ACTION -> {
                onTimeMinutesChange(context)
            }
            ALARM_REMIND_CLICK_ACTION -> {
                Status.isStartAlarmReminder = !Status.isStartAlarmReminder
                onAlarmReminderToggle(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                onAlarmReminderToggle(context)
                onTimeMinutesChange(context)
            }
        }
    }

    private fun registerTimeRemindAction(context: Context): PendingIntent {
        //注册广播
        val intent = Intent(TIME_REMIND_CLICK_ACTION).apply {
            component = ComponentName(
                context,
                YuzusoftTimeRemindWidgetProvider::class.java
            )
        }

        return PendingIntent.getBroadcast(
            context,
            R.id.time_remind_controller,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun registerAlarmRemindAction(context: Context): PendingIntent {
        //注册广播
        val intent = Intent(ALARM_REMIND_CLICK_ACTION).apply {
            component = ComponentName(
                context,
                YuzusoftTimeRemindWidgetProvider::class.java
            )
        }

        return PendingIntent.getBroadcast(
            context,
            R.id.alarm_remind_controller,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun onTimeReminderToggle(context: Context) {

        updateRemoteViewImage(R.id.time_remind_controller, context, if (Status.isStartTimeReminder) {

            onTimeMinutesChange(context)

            if (Build.VERSION.SDK_INT >= 26) {

                context.startForegroundService(
                    Intent(
                        context,
                        TimeInsureService::class.java
                    )
                )
            } else {
                context.startService(
                    Intent(
                        context,
                        TimeInsureService::class.java
                    )
                )
            }

            R.drawable.ic_volume_up_pink_24dp
        } else {

            context.stopService(
                Intent(
                    context,
                    TimeInsureService::class.java
                )
            )

            R.drawable.ic_volume_off_pink_24dp

        })
    }

    private fun onAlarmReminderToggle(context: Context) {

        updateRemoteViewImage(R.id.alarm_remind_controller, context, if (Status.isStartAlarmReminder) {

            onTimeMinutesChange(context)

            if (Build.VERSION.SDK_INT >= 26) {

                context.startForegroundService(
                    Intent(
                        context,
                        TimeAlarmService::class.java
                    )
                )
            } else {
                context.startService(
                    Intent(
                        context,
                        TimeAlarmService::class.java
                    )
                )
            }

            R.drawable.ic_alarm_on_pink_24dp
        } else {

            context.stopService(
                Intent(
                    context,
                    TimeAlarmService::class.java
                )
            )

            R.drawable.ic_alarm_off_pink_24dp

        })
    }

    private fun onTimeMinutesChange(context: Context) {
        updateRemoteText(context, formatTimeString())
    }

    private fun updateRemoteViewImage(viewId: Int, context: Context, resourceId: Int) {

        if (!this::remoteViews.isInitialized) {
            remoteViews = RemoteViews(context.packageName, R.layout.time_remind_widget_layout)
        }

        remoteViews.setImageViewResource(
            viewId,
            resourceId
        )

        val componentName = ComponentName(context, YuzusoftTimeRemindWidgetProvider::class.java)
        AppWidgetManager.getInstance(context)
            .updateAppWidget(componentName, remoteViews)
    }

    private fun updateRemoteText(context: Context, text: String) {
        if (!this::remoteViews.isInitialized) {
            remoteViews = RemoteViews(context.packageName, R.layout.time_remind_widget_layout)
        }

        remoteViews.setTextViewText(
            R.id.time_text,
            text
        )

        val componentName = ComponentName(context, YuzusoftTimeRemindWidgetProvider::class.java)
        AppWidgetManager.getInstance(context)
            .updateAppWidget(componentName, remoteViews)
    }

    private fun formatTimeString(): String {
        return "${
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12) {
                "下午"
            } else {
                "上午"
            }
        } ${
            Calendar.getInstance().run {
                "${
                if (get(Calendar.HOUR) > 12) {
                    get(Calendar.HOUR) - 12
                } else {
                    get(Calendar.HOUR)
                }
                }:${
                    "${
                        if (get(Calendar.MINUTE) < 10) {
                            "0"
                        } else {
                            ""
                        }
                    }${get(Calendar.MINUTE)}"
                }"
            }
        }"
    }

    companion object {
        const val TIME_REMIND_CLICK_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_CLICK" // 点击事件的广播ACTION
        const val ALARM_REMIND_CLICK_ACTION = "com.yumetsuki.yuzusoftappwidget.action.ALARM_REMIND_CLICK"
        const val UPDATE_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_UPDATE" // 更新事件的广播ACTION
//        const val NEW_ALARM_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_NEW_ALARM"
//        const val UPDATE_ALARM_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_UPDATE_ALARM"
//        const val REMOVE_ALARM_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_REMOVE_ALARM"
//        const val TIME_VOICE_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_VOICE_ACTION" //报时任务ACTION
    }

}