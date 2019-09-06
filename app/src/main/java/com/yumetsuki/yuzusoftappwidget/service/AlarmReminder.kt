package com.yumetsuki.yuzusoftappwidget.service

import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yumetsuki.yuzusoftappwidget.AppContext
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.page.alarm_lock.AlarmLockActivity
import com.yumetsuki.yuzusoftappwidget.repo.AlarmRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays
import com.yumetsuki.yuzusoftappwidget.utils.toJson
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AlarmReminder(private val context: Context) {

    private val alarmRepository = AlarmRepository.create(context)

//    private val alarmJobs: HashMap<Int, Job> = hashMapOf()

    private val alarms: ArrayList<Int> = arrayListOf()

    fun initAllAlarm() {
//        alarmJobs.forEach {
//            it.value.cancel()
//        }
//        alarmJobs.clear()
        GlobalScope.launch {
            alarmRepository.getAllAlarms().filter { it.alarmSetting.isEnable }
                .forEach {
//                    alarmJobs[it.alarmSetting.id] = createAlarmJob(it)
                    cancelAlarmJob(it.alarmSetting.id)
                    createAlarmJob(it)
                    alarms.add(it.alarmSetting.id)
                }
        }
    }

    fun newAlarm(id: Int) {
        GlobalScope.launch {
            alarmRepository.getAlarmById(id).apply {
//                alarmJobs[alarmSetting.id] = createAlarmJob(this)
                createAlarmJob(this)
                alarms.add(alarmSetting.id)
            }
        }
    }

    fun updateAlarm(id: Int) {
        GlobalScope.launch {
            alarmRepository.getAlarmById(id).apply {
                cancelAlarmJob(id)
                if (alarmSetting.isEnable) {
//                    alarmJobs[alarmSetting.id] = createAlarmJob(this)
                    createAlarmJob(this)
                }
            }
        }
    }

    fun removeAlarm(id: Int) {
        cancelAlarmJob(id)
        alarms.removeAll { it == id }
//        alarmJobs.remove(id)
    }

    fun removeAllAlarmJob() {
//        alarmJobs.forEach {
//            it.value.cancel()
//        }
//        alarmJobs.clear()
        alarms.forEach {
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(getAlarmWorkerTag(it))
        }
        alarms.clear()
    }

    private fun createAlarmJob(alarmSettingModel: AlarmSettingModel): Job {
        return GlobalScope.launch {
//            while (true) {
//                delay(
//                    getNextAlarmDelay(
//                        alarmSettingModel.days,
//                        alarmSettingModel.alarmSetting.alarmHour,
//                        alarmSettingModel.alarmSetting.alarmMinute
//                    )
//                )
//                startAlarm(alarmSettingModel)
//            }

            WorkManager.getInstance(context)
                .enqueue(
                    OneTimeWorkRequestBuilder<AlarmVoiceWorker>()
                        .setInitialDelay(
                            getNextAlarmDelay(
                                alarmSettingModel.days,
                                alarmSettingModel.alarmSetting.alarmHour,
                                alarmSettingModel.alarmSetting.alarmMinute
                            ),
                            TimeUnit.MILLISECONDS
                        ).addTag(
                            getAlarmWorkerTag(alarmSettingModel.alarmSetting.id)
                        ).setInputData(
                            Data.Builder().putString(
                                AlarmVoiceWorker.DATA_KEY,
                                alarmSettingModel.toJson()
                            ).build()
                        ).build()
                )
        }
    }

    private fun startAlarm(alarmSettingModel: AlarmSettingModel) {
        Intent(AppContext, AlarmLockActivity::class.java).apply {
            //启动新任务，并避免重复启动
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            putExtra(AlarmLockActivity.ALARM_SETTING_MODEL_EXTRA, alarmSettingModel)
            context.startActivity(this)
        }
    }

    private fun cancelAlarmJob(id: Int) {
//        alarmJobs[id]?.cancel()
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(getAlarmWorkerTag(id))
    }

    companion object {

        /**
         * 获取闹钟定时任务tag
         * */
        fun getAlarmWorkerTag(alarmId: Int): String {
            return "${AlarmVoiceWorker.TAG}-$alarmId"
        }

        /**
         * 获取到目标闹钟响起的延时，单位为毫秒
         * */
        fun getNextAlarmDelay(days: List<AlarmSettingDays>, hour: Int, minute: Int): Long {
            return Calendar.getInstance().run {
                var timeDelay = 0L
                while (true) {
                    //原始值为周日到周一1～7，转换后为0～6
                    val currentDay = get(Calendar.DAY_OF_WEEK) - 1
                    days.map {
                        it.day % 7
                    }.map {
                        getDateInterval(it, currentDay, timeDelay)
                    }.min()!!.also {

                        val targetTime = getTargetTime(it, hour, minute)
                        //这里后者不采用this.timeInMillis时间，因为此时计算时间已经有了变化
                        timeDelay = targetTime.timeInMillis - Calendar.getInstance().timeInMillis

                    }
                    //计算成功
                    if (timeDelay > 0) break
                }
                timeDelay
            }
        }

        /**
         * 计算闹钟响起目标时间
         * */
        private fun getTargetTime(appendDay: Int, hour: Int, minute: Int): Calendar {
            return Calendar.getInstance().apply {
                set(
                    getTargetYear(this, appendDay),
                    getTargetMonth(this, appendDay),
                    getTargetDay(this, appendDay),
                    hour,
                    minute,
                    0
                )
            }
        }

        /**
         * 计算闹钟所定重复日期到当前日期的间隔
         * */
        private fun getDateInterval(day: Int, currentDay: Int, timeDelay: Long): Int {
            return if (day >= currentDay) {
                //出现过一次延时小于0的情况，说明至少该次闹钟要轮到一周之后
                if (timeDelay < 0 && day == currentDay) {
                    7
                } else {
                    day - currentDay
                }
            } else {
                7 - currentDay + day
            }
        }

        /**
         * 获取闹钟目标年份
         * */
        private fun getTargetYear(calendar: Calendar, appendDay: Int): Int {
            val year = calendar[Calendar.YEAR]
            val totalDayOfYear = if (isLeapYear(year)) {
                366
            } else {
                365
            }
            val appendYear = if (calendar[Calendar.DAY_OF_YEAR] + appendDay > totalDayOfYear) {
                1
            } else {
                0
            }
            return calendar[Calendar.YEAR] + appendYear
        }

        /**
         * 获取闹钟目标月份
         * */
        private fun getTargetMonth(calendar: Calendar, appendDay: Int): Int {
            val totalDayOfMonth = getMonthDayCount(calendar[Calendar.YEAR], calendar[Calendar.MONTH])
            val appendMonth = if (calendar[Calendar.MONTH] + appendDay > totalDayOfMonth) {
                1
            } else {
                0
            }
            return (calendar[Calendar.MONTH] + appendMonth) % 12
        }

        /**
         * 获取闹钟目标日期
         * */
        private fun getTargetDay(calendar: Calendar, appendDay: Int): Int {
            val totalDayOfMonth = getMonthDayCount(calendar[Calendar.YEAR], calendar[Calendar.MONTH])
            return ((calendar[Calendar.DAY_OF_MONTH] + appendDay) % totalDayOfMonth).let {
                if (it == 0) {
                    1
                } else {
                    it
                }
            }
        }

        /**
         * 判断闰年
         * */
        private fun isLeapYear(year: Int) = year % 4 == 0 && year % 100 != 0 || year % 400 == 0

        /**
         * 获取月的天数
         * */
        private fun getMonthDayCount(year: Int, month: Int): Int {
            return when(month) {
                0 -> 31
                1 -> if (isLeapYear(year)) 29 else 28
                2 -> 31
                3 -> 30
                4 -> 31
                5 -> 30
                6 -> 31
                7 -> 31
                8 -> 30
                9 -> 31
                10 -> 30
                11 -> 31
                else -> error("no month!")
            }
        }
    }

}