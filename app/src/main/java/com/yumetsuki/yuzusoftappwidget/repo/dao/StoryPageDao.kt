package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryPage
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryPage

@Dao
interface StoryPageDao {

    @Insert(entity = StoryPage::class)
    suspend fun insertStoryPage(vararg noIdStoryPage: NoIdStoryPage)

    @Delete
    suspend fun deleteStoryPage(vararg storyPage: StoryPage)

    @Update
    suspend fun updateStoryPage(vararg storyPage: StoryPage)

    @Query("SELECT * FROM story_page WHERE chapter_id=:chapterId")
    suspend fun queryStoryPagesByChapterId(chapterId: Int): List<StoryPage>

}