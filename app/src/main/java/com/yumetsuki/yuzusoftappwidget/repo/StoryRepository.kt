package com.yumetsuki.yuzusoftappwidget.repo

import android.content.Context
import androidx.annotation.WorkerThread
import com.yumetsuki.yuzusoftappwidget.model.NoIdStory
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryChapter
import com.yumetsuki.yuzusoftappwidget.repo.dao.StoryChapterDao
import com.yumetsuki.yuzusoftappwidget.repo.dao.StoryCharacterDao
import com.yumetsuki.yuzusoftappwidget.repo.dao.StoryDao
import com.yumetsuki.yuzusoftappwidget.repo.dao.StoryPageDao
import com.yumetsuki.yuzusoftappwidget.repo.database.YuzuSoftAppWidgetDatabase
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.utils.toNoId

class StoryRepository(
    private val storyDao: StoryDao,
    private val storyChapterDao: StoryChapterDao,
    private val storyPageDao: StoryPageDao,
    private val storyCharacterDao: StoryCharacterDao
) {

    @WorkerThread
    suspend fun getStories() = storyDao.queryStories()

    suspend fun addStory(noIdStory: NoIdStory) {
        storyDao.insertStory(noIdStory)
    }

    suspend fun removeStory(story: Story) {
        storyDao.deleteStory(story)
    }

    suspend fun getChaptersByStoryId(storyId: Int) = storyChapterDao.queryStoryChaptersByStoryId(storyId)

    suspend fun addChapter(noIdStoryChapter: NoIdStoryChapter) {
        storyChapterDao.insertStoryChapter(noIdStoryChapter)
    }

    companion object {

        fun create(context: Context): StoryRepository {
            return YuzuSoftAppWidgetDatabase.getDatabase(context).run {
                StoryRepository(
                    storyDao(),
                    storyChapterDao(),
                    storyPageDao(),
                    storyCharacterDao()
                )
            }
        }

    }
}