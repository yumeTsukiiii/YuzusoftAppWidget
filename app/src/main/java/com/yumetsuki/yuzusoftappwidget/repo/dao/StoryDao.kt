package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdStory
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story

@Dao
interface StoryDao {

    @Query("SELECT * FROM story")
    suspend fun queryStories(): List<Story>

    @Query("SELECT * FROM story WHERE id=:id")
    suspend fun queryStoryById(id: Int): Story

    @Update
    suspend fun updateStory(vararg story: Story)

    @Delete
    suspend fun deleteStory(vararg story: Story)

    @Insert(entity = Story::class)
    suspend fun insertStory(vararg noIdStory: NoIdStory)

}