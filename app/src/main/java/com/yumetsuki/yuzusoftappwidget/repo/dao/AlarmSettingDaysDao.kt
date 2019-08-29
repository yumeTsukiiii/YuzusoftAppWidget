package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays

@Dao
interface AlarmSettingDaysDao {

    @Query("SELECT * FROM alarm_setting_days WHERE setting_id = :id")
    fun getDaysByAlarmSettingId(id: Int): List<AlarmSettingDays>

    @Insert
    suspend fun insertDays(vararg days: AlarmSettingDays)

    @Delete
    suspend fun deleteDays(vararg alarmSettingDays: AlarmSettingDays)

}