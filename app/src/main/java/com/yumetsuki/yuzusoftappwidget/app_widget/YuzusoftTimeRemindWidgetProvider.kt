package com.yumetsuki.yuzusoftappwidget.app_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.widget.RemoteViews
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yumetsuki.yuzusoftappwidget.CharacterConfig
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.service.TimeInsureService
import com.yumetsuki.yuzusoftappwidget.service.TimeRemindWork
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

        //注册广播
        val intent = Intent(CLICK_ACTION).apply {
            component = ComponentName(
                context,
                YuzusoftTimeRemindWidgetProvider::class.java
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            R.id.time_remind_controller,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        remoteViews.setOnClickPendingIntent(
            R.id.time_remind_controller,
            pendingIntent
        )

        remoteViews.setImageViewResource(
            R.id.time_remind_controller,
            if (Status.isStartTimeReminder) {
                R.drawable.ic_volume_up_pink_24dp
            } else {
                R.drawable.ic_volume_off_pink_24dp
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
        context.stopService(
            Intent(
                context,
                TimeInsureService::class.java
            )
        )
        WorkManager.getInstance(context).apply {
            cancelAllWorkByTag(TimeRemindWork.TAG)
            pruneWork()
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            //点击开关报时
            CLICK_ACTION -> {
                onTimeReminderToggle(context)
            }
            //老婆语音报时
            TIME_VOICE_ACTION -> {
                onVoiceTime(context)
            }
            //更新时间数字
            UPDATE_ACTION -> {
                onTimeMinutesChange(context)
            }
        }
    }

    private fun onTimeReminderToggle(context: Context) {

        Status.isStartTimeReminder = !Status.isStartTimeReminder

        updateRemoteViewImage(context, if (Status.isStartTimeReminder) {

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

            WorkManager.getInstance(context)
                .enqueue(OneTimeWorkRequestBuilder<TimeRemindWork>().addTag(TimeRemindWork.TAG).build())

            R.drawable.ic_volume_up_pink_24dp
        } else {

            context.stopService(
                Intent(
                    context,
                    TimeInsureService::class.java
                )
            )

            WorkManager.getInstance(context).apply {
                cancelAllWorkByTag(TimeRemindWork.TAG)
                pruneWork()
            }

            R.drawable.ic_volume_off_pink_24dp

        })
    }

    private fun onVoiceTime(context: Context) {

        Wife.values().find { it.wifeName == CharacterConfig.mostLikeWife }?.also { wife ->

            Calendar.getInstance().apply {

                Status.isCharacterPlaying = true

                //播放午别的音频
                MediaPlayer.create(
                    context,
                    if (get(Calendar.HOUR_OF_DAY) > 12) {
                        wife.timerVoice.afternoon
                    } else {
                        wife.timerVoice.morning
                    }
                ).apply {

                    setOnCompletionListener {
                        //播放小时的音频
                        MediaPlayer.create(
                            context,
                            wife.timerVoice.hours[
                                    if (get(Calendar.HOUR) == 0) {
                                        11
                                    } else {
                                        get(Calendar.HOUR) - 1
                                    }
                            ]
                        ).apply {

                            setOnCompletionListener {
                                //播放分钟的音频
                                wife.timerVoice.minutes[get(Calendar.MINUTE)].iterator()
                                    .apply {

                                        fun forEachMinutesVoice(hasNext: Boolean) {
                                            if (hasNext) {
                                                MediaPlayer.create(
                                                    context,
                                                    next()
                                                ).apply {
                                                    setOnCompletionListener {
                                                        forEachMinutesVoice(hasNext())
                                                    }
                                                }.start()
                                            } else {
                                                //播放句尾
                                                MediaPlayer.create(
                                                    context,
                                                    wife.timerVoice.desu
                                                ).apply {
                                                    setOnCompletionListener {
                                                        Status.isCharacterPlaying = false
                                                    }
                                                }.start()
                                            }
                                        }

                                        forEachMinutesVoice(hasNext())

                                    }
                            }

                        }.start()
                    }

                }.start()

            }

        }

    }

    private fun onTimeMinutesChange(context: Context) {
        updateRemoteText(context, formatTimeString())
    }

    private fun updateRemoteViewImage(context: Context, resourceId: Int) {

        if (!this::remoteViews.isInitialized) {
            remoteViews = RemoteViews(context.packageName, R.layout.time_remind_widget_layout)
        }

        remoteViews.setImageViewResource(
            R.id.time_remind_controller,
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
        const val CLICK_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_CLICK" // 点击事件的广播ACTION
        const val UPDATE_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_REMIND_UPDATE" // 更新事件的广播ACTION
        const val TIME_VOICE_ACTION = "com.yumetsuki.yuzusoftappwidget.action.TIME_VOICE_ACTION" //报时任务ACTION
    }

}