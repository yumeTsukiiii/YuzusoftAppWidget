package com.yumetsuki.yuzusoftappwidget.service

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yumetsuki.yuzusoftappwidget.CharacterConfig
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.utils.playMediaSequenceAsync
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*

object TimeReminder {

    private var remindJob: Job = Job()

    fun start(context: Context) {
//        remindJob = GlobalScope.launch {
//            tryToRemindAtTime(context)
//            delayToNextHour()
//        }
        WorkManager.getInstance(context)
            .enqueue(
                OneTimeWorkRequestBuilder<TimerVoiceWorker>()
                    .addTag(TimerVoiceWorker.TAG)
                    .build())
    }

    fun stop(context: Context) {
//        remindJob.cancel()
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(TimerVoiceWorker.TAG)
    }

    private fun tryToRemindAtTime(context: Context, timeRange: Int = 0) {
        if (Calendar.getInstance().get(Calendar.MINUTE) <= timeRange) {
            GlobalScope.launch {
                playVoiceAtTime(context)
            }
        }
    }

    private suspend fun delayToNextHour() {
        delay(
            Calendar.getInstance().run {
                ((60 - get(Calendar.SECOND)) +
                        (59 - get(Calendar.MINUTE)) * 60)
                    .toLong() * 1000
            }
        )
    }

    suspend fun playVoiceAtTime(context: Context) {

        Wife.values().find { it.wifeName == CharacterConfig.mostLikeWifeName }?.let { wife ->

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

}