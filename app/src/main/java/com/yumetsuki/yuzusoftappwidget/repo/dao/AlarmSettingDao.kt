package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting

@Dao
interface AlarmSettingDao {

    @Query("SELECT * FROM alarm_setting")
    suspend fun getAllAlarmSettings(): List<AlarmSetting>

    @Insert
    suspend fun insertAlarmSetting(vararg alarmSetting: AlarmSetting)

    @Delete
    suspend fun deleteAlarmSetting(vararg alarmSetting: AlarmSetting)

    @Update
    suspend fun updateAlarmSetting(vararg alarmSetting: AlarmSetting)


}