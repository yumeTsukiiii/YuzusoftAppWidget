package com.yumetsuki.yuzusoftappwidget.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.yumetsuki.yuzusoftappwidget.AlarmTaskInterface
import com.yumetsuki.yuzusoftappwidget.AppContext
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.broadcast.MinutesTimerBroadcast
import com.yumetsuki.yuzusoftappwidget.repo.AlarmRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 该服务用来启动每分钟定时触发的广播，并确保应用程序所发出的任务的执行
 * */
class TimeAlarmService: Service() {

    private val channelId = "time_alarm_service_id"

    private val channelName = "time_alarm_service_name"

    private val notificationId = 2

    private val alarmReminder: AlarmReminder by lazy {
        AlarmReminder(this)
    }

    private val alarmTaskInterface by lazy {
        object : AlarmTaskInterface.Stub() {

            override fun updateAlarm(id: Int) {
                alarmReminder.updateAlarm(id)
            }

            override fun newAlarm(id: Int) {
                alarmReminder.newAlarm(id)
            }

            override fun removeAlarm(id: Int) {
                alarmReminder.removeAlarm(id)
            }

        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return alarmTaskInterface
    }

    override fun onCreate() {
        super.onCreate()

        val notificationBuilder: Notification.Builder = generateNotificationBuilder()

        /**开启前台服务*/
        notificationBuilder.apply {
            setContentTitle("柚子小部件")
            setContentText("正在进行闹钟监听～通知不见了要重启哦～")
            setSmallIcon(R.drawable.murasame_icon)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.murasame_icon))
        }.build().also {
            startForeground(notificationId, it)
        }

        /**开启闹钟任务*/
        alarmReminder.initAllAlarm()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmReminder.removeAllAlarmJob()
        stopForeground(true)
    }

    private fun generateNotificationBuilder(): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= 26) {

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .apply {
                    createNotificationChannel(
                        NotificationChannel(
                            channelId,
                            channelName,
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    )
                }

            Notification.Builder(this, channelId)

        } else {
            Notification.Builder(this)
        }
    }

}