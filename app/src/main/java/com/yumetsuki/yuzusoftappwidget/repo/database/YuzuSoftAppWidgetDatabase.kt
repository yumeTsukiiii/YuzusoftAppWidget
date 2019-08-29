package com.yumetsuki.yuzusoftappwidget.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yumetsuki.yuzusoftappwidget.repo.dao.AlarmSettingDao
import com.yumetsuki.yuzusoftappwidget.repo.dao.AlarmSettingDaysDao
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays

@Database(
    entities = [
        AlarmSetting::class,
        AlarmSettingDays::class
    ],
    version = 1
)
abstract class YuzuSoftAppWidgetDatabase: RoomDatabase() {

    abstract fun alarmSettingDao(): AlarmSettingDao

    abstract fun alarmSettingDaysDao(): AlarmSettingDaysDao

    companion object {

        @Volatile
        private var INSTANCE: YuzuSoftAppWidgetDatabase? = null

        fun getDatabase(context: Context): YuzuSoftAppWidgetDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    YuzuSoftAppWidgetDatabase::class.java,
                    "Word_database"
                ).build().apply {
                    INSTANCE = this
                }
            }
        }

    }

}