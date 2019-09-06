package com.yumetsuki.yuzusoftappwidget.service

import android.content.Context
import android.content.Intent
import androidx.work.*
import com.yumetsuki.yuzusoftappwidget.AppContext
import com.yumetsuki.yuzusoftappwidget.Status
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.page.alarm_lock.AlarmLockActivity
import com.yumetsuki.yuzusoftappwidget.utils.fromJson
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * 用于执行闹钟任务的Worker
 * */
class AlarmVoiceWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        /**如果有正在进行的语音播报，则稍等一会*/
        if (Status.isCharacterPlaying) {
            delay(3000)
        }

        inputData.getString(DATA_KEY)!!.also {
            it.fromJson<AlarmSettingModel>()!!.apply {

                //计算下一次闹钟的时间
                WorkManager.getInstance(applicationContext)
                    .enqueue(
                        OneTimeWorkRequestBuilder<AlarmVoiceWorker>()
                            .setInitialDelay(
                                AlarmReminder.getNextAlarmDelay(days, alarmSetting.alarmHour, alarmSetting.alarmMinute),
                                TimeUnit.MILLISECONDS
                            ).addTag(
                                AlarmReminder.getAlarmWorkerTag(alarmSetting.id)
                            ).setInputData(
                                Data.Builder()
                                    .putString(DATA_KEY, it)
                                    .build()
                            ).build()
                    )

                startAlarm(this)
            }

        }

        return Result.success()
    }

    /**
     * 开启锁屏activity进行闹钟报时
     * */
    private fun startAlarm(alarmSettingModel: AlarmSettingModel) {
        Intent(AppContext, AlarmLockActivity::class.java).apply {
            //启动新任务，并避免重复启动
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            putExtra(AlarmLockActivity.ALARM_SETTING_MODEL_EXTRA, alarmSettingModel)
            applicationContext.startActivity(this)
        }
    }

    companion object {
        const val DATA_KEY = "com.yumetsuki.yuzusoftappwidget.service.AlarmVoiceWorker.AlarmSettingModel"
        const val TAG = "com.yumetsuki.yuzusoftappwidget.service.AlarmVoiceWorker"
    }
}