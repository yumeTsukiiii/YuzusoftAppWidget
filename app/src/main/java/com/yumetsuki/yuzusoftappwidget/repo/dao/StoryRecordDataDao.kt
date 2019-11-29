package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdPreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryRecordData
import com.yumetsuki.yuzusoftappwidget.repo.entity.PreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryRecordData

@Dao
interface StoryRecordDataDao {

    @Insert(entity = StoryRecordData::class)
    suspend fun insertStoryRecordData(vararg noIdStoryRecordData: NoIdStoryRecordData)

    @Delete
    suspend fun deleteStoryRecordData(vararg storyRecordData: StoryRecordData)

    @Update
    suspend fun updateStoryRecordData(vararg storyRecordData: StoryRecordData)

    @Query("SELECT * FROM story_record_data WHERE story_id=:storyId")
    suspend fun queryStoryRecordDataByStoryId(storyId: Int): List<StoryRecordData>

}