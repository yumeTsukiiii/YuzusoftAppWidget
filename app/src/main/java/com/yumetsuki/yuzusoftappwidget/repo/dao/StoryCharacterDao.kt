package com.yumetsuki.yuzusoftappwidget.repo.dao

import androidx.room.*
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryCharacter
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryCharacter

@Dao
interface StoryCharacterDao {

    @Insert(entity = StoryCharacter::class)
    suspend fun insertStoryCharacter(vararg noIdStoryCharacter: NoIdStoryCharacter)

    @Delete
    suspend fun deleteStoryCharacter(vararg storyCharacter: StoryCharacter)

    @Update
    suspend fun updateStoryCharacter(vararg storyCharacter: StoryCharacter)

    @Query("SELECT * FROM story_character WHERE page_id=:pageId")
    suspend fun queryStoryCharacterByPageId(pageId: Int): List<StoryCharacter>

}