package com.yumetsuki.yuzusoftappwidget.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.yumetsuki.yuzusoftappwidget.app_widget.YuzusoftTimeRemindWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class TimeRemindWork(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        if (Calendar.getInstance().get(Calendar.MINUTE) <= 5) {
            //发送整点报时广播
            applicationContext.sendBroadcast(Intent(YuzusoftTimeRemindWidgetProvider.TIME_VOICE_ACTION).apply {
                component = ComponentName(
                    applicationContext,
                    YuzusoftTimeRemindWidgetProvider::class.java
                )
            })
        }

        Calendar.getInstance().apply {
            WorkManager.getInstance(applicationContext)
                .enqueue(
                    OneTimeWorkRequestBuilder<TimeRemindWork>()
                    .setInitialDelay(
                        ((60 - get(Calendar.SECOND)) + (59 - get(Calendar.MINUTE)) * 60).toLong(),
                        TimeUnit.SECONDS
                    ).addTag(TAG).build()
                )
        }
        return Result.success()

    }

    companion object {
        const val TAG = "com.yumetsuki.yuzusoftappwidget.service.TimeRemindWork"
    }

}