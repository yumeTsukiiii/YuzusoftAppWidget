package com.yumetsuki.yuzusoftappwidget.repo

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.repo.dao.AlarmSettingDao
import com.yumetsuki.yuzusoftappwidget.repo.dao.AlarmSettingDaysDao
import com.yumetsuki.yuzusoftappwidget.repo.database.YuzuSoftAppWidgetDatabase
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays
import com.yumetsuki.yuzusoftappwidget.utils.toNoId

class AlarmRepository private constructor(
    private val alarmSettingDao: AlarmSettingDao,
    private val alarmSettingDaysDao: AlarmSettingDaysDao
) {

    @WorkerThread
    suspend fun getAllAlarms(): List<AlarmSettingModel> {
        return alarmSettingDao.getAllAlarmSettings().map { alarmSetting ->
            AlarmSettingModel(
                alarmSetting,
                alarmSettingDaysDao.getDaysByAlarmSettingId(alarmSetting.id)
            )
        }
    }

    @WorkerThread
    suspend fun getAlarmById(id: Int): AlarmSettingModel {
        return AlarmSettingModel(
            alarmSettingDao.getAlarmSettingById(id)[0],
            alarmSettingDaysDao.getDaysByAlarmSettingId(id)
        )
    }

    @WorkerThread
    suspend fun insertAlarm(alarmSettingModel: AlarmSettingModel) {

        alarmSettingDao.insertAlarmSetting(alarmSettingModel.alarmSetting.toNoId())

        alarmSettingDao.getAllAlarmSettings().last().also { alarmSetting ->
            alarmSettingDaysDao.insertDays(
                *alarmSettingModel.days.map { AlarmSettingDays(0, it.day ,alarmSetting.id).toNoId() }
                    .toTypedArray()
            )
        }
    }

    suspend fun deleteAlarm(alarmSettingModel: AlarmSettingModel) {

        alarmSettingDaysDao.deleteDays(
            *alarmSettingModel.days.toTypedArray()
        )

        alarmSettingDao.deleteAlarmSetting(alarmSettingModel.alarmSetting)

    }

    suspend fun updateAlarm(
        oldAlarm: AlarmSettingModel,
        newAlarm: AlarmSettingModel
    ) {
        alarmSettingDaysDao.deleteDays(*oldAlarm.days.toTypedArray())
        alarmSettingDaysDao.insertDays(*newAlarm.days.map { it.toNoId() }.toTypedArray())
        alarmSettingDao.updateAlarmSetting(newAlarm.alarmSetting)

    }

    suspend fun toggleAlarm(
        alarm: AlarmSettingModel
    ) {
        alarmSettingDao.updateAlarmSetting(
            AlarmSetting(
                alarm.alarmSetting.id,
                alarm.alarmSetting.icon,
                alarm.alarmSetting.alarmHour,
                alarm.alarmSetting.alarmMinute,
                !alarm.alarmSetting.isEnable
            )
        )
    }

    companion object {

        fun create(context: Context): AlarmRepository {
            return YuzuSoftAppWidgetDatabase.getDatabase(context).run {
                AlarmRepository(
                    alarmSettingDao(),
                    alarmSettingDaysDao()
                )
            }
        }

    }

}