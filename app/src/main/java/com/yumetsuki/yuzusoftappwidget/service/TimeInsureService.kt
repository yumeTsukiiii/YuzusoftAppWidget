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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.broadcast.MinutesTimerBroadcast

/**
 * 该服务用来启动每分钟定时触发的广播，并确保应用程序所发出的任务的执行
 * */
class TimeInsureService: Service() {

    private val channelId = "foreground_service_id"

    private val channelName = "foreground_service_name"

    private val notificationId = 1

    private val receiver: MinutesTimerBroadcast = MinutesTimerBroadcast()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val notificationBuilder: Notification.Builder = if (Build.VERSION.SDK_INT >= 26) {

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

        notificationBuilder.apply {
            setContentTitle("柚子小部件")
            setContentText("正在进行报时监听～通知不见了要重启哦～")
            setSmallIcon(R.drawable.murasame_icon)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.murasame_icon))
        }.build().also {
            startForeground(notificationId, it)
        }

        applicationContext.registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            //提升优先级，尽可能每分钟能触发任务
            priority = 1000
        })

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        stopForeground(true)
    }


}