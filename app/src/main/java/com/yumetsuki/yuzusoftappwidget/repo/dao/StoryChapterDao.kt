package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryChapter
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryChapter

@Dao
interface StoryChapterDao {

    @Insert(entity = StoryChapter::class)
    suspend fun insertStoryChapter(vararg noIdStoryChapter: NoIdStoryChapter)

    @Delete
    suspend fun deleteStoryChapter(vararg storyChapter: StoryChapter)

    @Update
    suspend fun updateStoryChapter(vararg storyChapter: StoryChapter)

    @Query("SELECT * FROM STORY_CHAPTER WHERE story_id=:storyId")
    suspend fun queryStoryChaptersByStoryId(storyId: Int): List<StoryChapter>

}