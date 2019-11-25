package com.yumetsuki.yuzusoftappwidget.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yumetsuki.yuzusoftappwidget.repo.dao.*
import com.yumetsuki.yuzusoftappwidget.repo.entity.*

@Database(
    entities = [
        AlarmSetting::class,
        AlarmSettingDays::class,
        Story::class,
        StoryPage::class,
        StoryChapter::class,
        StoryCharacter::class
    ],
    version = 1
)
abstract class YuzuSoftAppWidgetDatabase: RoomDatabase() {

    abstract fun alarmSettingDao(): AlarmSettingDao

    abstract fun alarmSettingDaysDao(): AlarmSettingDaysDao

    abstract fun storyDao(): StoryDao

    abstract fun storyChapterDao(): StoryChapterDao

    abstract fun storyPageDao(): StoryPageDao

    abstract fun storyCharacterDao(): StoryCharacterDao

    companion object {

        @Volatile
        private var INSTANCE: YuzuSoftAppWidgetDatabase? = null

        fun getDatabase(context: Context): YuzuSoftAppWidgetDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    YuzuSoftAppWidgetDatabase::class.java,
                    "yuzusoft_app_widget"
                ).build().apply {
                    INSTANCE = this
                }
            }
        }

    }

}