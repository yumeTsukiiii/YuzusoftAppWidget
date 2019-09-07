package com.yumetsuki.yuzusoftappwidget.service

import android.content.Context
import androidx.work.*
import com.yumetsuki.yuzusoftappwidget.CharacterConfig
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.utils.playMediaSequenceAsync
import kotlinx.coroutines.flow.collect
import java.util.*
import java.util.concurrent.TimeUnit

class TimerVoiceWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val isServiceRunning = Status.isStartTimeReminder

        val calendar = Calendar.getInstance()

        if (calendar[Calendar.MINUTE] <= 5 && isServiceRunning) {
            TimeReminder.playVoiceAtTime(applicationContext)
        }

        if (isServiceRunning) {
            WorkManager.getInstance(applicationContext)
                .enqueue(
                    OneTimeWorkRequestBuilder<TimerVoiceWorker>()
                        .addTag(TAG)
                        .setInitialDelay(
                            ((60 - calendar[Calendar.SECOND]) + (59 - calendar[Calendar.MINUTE]) * 60).toLong(),
                            TimeUnit.SECONDS
                        ).build()
                )
        }

        return Result.success()
    }

    companion object {
        const val TAG = "com.yumetsuki.yuzusoftappwidget.service.TimerVoiceWorker"
    }

}