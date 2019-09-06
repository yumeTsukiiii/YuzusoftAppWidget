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

        val calendar = Calendar.getInstance()

        if (calendar[Calendar.MINUTE] <= 5) {
            playVoiceAtTime(applicationContext)
        }

        WorkManager.getInstance(applicationContext)
            .enqueue(
                OneTimeWorkRequestBuilder<TimerVoiceWorker>()
                    .addTag(TAG)
                    .setInitialDelay(
                        ((60 - calendar[Calendar.SECOND]) + (59 - calendar[Calendar.MINUTE]) * 60).toLong(),
                        TimeUnit.SECONDS
                    ).build()
            )

        return Result.success()
    }

    private suspend fun playVoiceAtTime(context: Context) {

        Wife.values().find { it.wifeName == CharacterConfig.mostLikeWife }?.let { wife ->

            Calendar.getInstance().apply {

                Status.isCharacterPlaying = true

                context.playMediaSequenceAsync {

                    yield(
                        if (get(Calendar.HOUR_OF_DAY) > 12) {
                            wife.timerVoice.afternoon
                        } else {
                            wife.timerVoice.morning
                        }
                    )

                    yield(
                        wife.timerVoice.hours[
                                if (get(Calendar.HOUR) == 0) {
                                    11
                                } else {
                                    get(Calendar.HOUR) - 1
                                }
                        ]
                    )

                    yieldAll(
                        wife.timerVoice.minutes[get(Calendar.MINUTE)]
                    )

                    yield(
                        wife.timerVoice.desu
                    )

                }.collect()

                Status.isCharacterPlaying = false

            }

        }?: error("no wife")

    }

    companion object {
        const val TAG = "com.yumetsuki.yuzusoftappwidget.service.TimerVoiceWorker"
    }

}