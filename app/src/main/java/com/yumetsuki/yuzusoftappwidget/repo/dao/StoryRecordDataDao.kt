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
    suspend fun deleteStoryRecordData(vararg previousEditRecord: StoryRecordData)

    @Update
    suspend fun updateStoryRecordData(vararg previousEditRecord: StoryRecordData)

    @Query("SELECT * FROM previous_edit_record WHERE id=:storyId")
    suspend fun queryStoryRecordDataByStoryId(storyId: Int): StoryRecordData?

}