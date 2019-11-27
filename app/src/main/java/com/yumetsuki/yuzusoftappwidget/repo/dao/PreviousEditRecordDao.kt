package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdPreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.entity.PreviousEditRecord

@Dao
interface PreviousEditRecordDao {

    @Insert(entity = PreviousEditRecord::class)
    suspend fun insertPreviousEditRecord(vararg noIdPreviousEditRecord: NoIdPreviousEditRecord)

    @Delete
    suspend fun deletePreviousEditRecord(vararg previousEditRecord: PreviousEditRecord)

    @Update
    suspend fun updatePreviousEditRecord(vararg previousEditRecord: PreviousEditRecord)

    @Query("SELECT * FROM previous_edit_record WHERE id=:storyId")
    suspend fun queryPreviousEditRecordByStoryId(storyId: Int): PreviousEditRecord?

}