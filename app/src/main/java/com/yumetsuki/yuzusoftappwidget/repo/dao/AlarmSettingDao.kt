package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdAlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting

@Dao
interface AlarmSettingDao {

    @Query("SELECT * FROM alarm_setting")
    suspend fun getAllAlarmSettings(): List<AlarmSetting>

    @Query("SELECT * FROM alarm_setting where id=:id")
    suspend fun getAlarmSettingById(id: Int): List<AlarmSetting>

    @Insert(entity = AlarmSetting::class)
    suspend fun insertAlarmSetting(vararg alarmSetting: NoIdAlarmSetting)

    @Delete
    suspend fun deleteAlarmSetting(vararg alarmSetting: AlarmSetting)

    @Update
    suspend fun updateAlarmSetting(vararg alarmSetting: AlarmSetting)


}