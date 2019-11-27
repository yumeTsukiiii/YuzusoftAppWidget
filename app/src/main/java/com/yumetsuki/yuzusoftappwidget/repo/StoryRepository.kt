package com.yumetsuki.yuzusoftappwidget.repo

import android.content.Context
import androidx.annotation.WorkerThread
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.*
import com.yumetsuki.yuzusoftappwidget.repo.dao.*
import com.yumetsuki.yuzusoftappwidget.repo.database.YuzuSoftAppWidgetDatabase
import com.yumetsuki.yuzusoftappwidget.repo.entity.PreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryCharacter
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryPage
import com.yumetsuki.yuzusoftappwidget.utils.toNoId

class StoryRepository(
    private val storyDao: StoryDao,
    private val storyChapterDao: StoryChapterDao,
    private val storyPageDao: StoryPageDao,
    private val storyCharacterDao: StoryCharacterDao,
    private val storyRecordDataDao: StoryRecordDataDao,
    private val previousEditRecordDao: PreviousEditRecordDao
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

    suspend fun getStoryPageModelByPageId(pageId: Int): StoryPageModel? {
        return storyPageDao.queryStoryPageById(pageId)?.let { page ->
            StoryPageModel(
                characterModels = storyCharacterDao.queryStoryCharacterByPageId(pageId).map {
                    StoryPageModel.StoryCharacterModel(
                        indexInPageView = it.indexInPageView,
                        wife = Wife.values().find { wife -> wife.wifeName == it.wifeName }!!,
                        clothesIndex = it.clothesIndex,
                        expressionIndex = it.expressionIndex,
                        scale = it.scale,
                        translateX = it.translateX,
                        translateY = it.translateY,
                        id = it.id,
                        pageId = it.pageId
                    )
                } as ArrayList,
                header = page.header,
                content = page.content,
                background = page.background,
                chapterId = page.chapterId,
                id = pageId
            )
        }
    }

    suspend fun getStoryPagesByChapterId(chapterId: Int): List<StoryPage> {
        return storyPageDao.queryStoryPagesByChapterId(
            chapterId
        )
    }

    suspend fun getStoryPageModelsByChapterId(chapterId: Int): List<StoryPageModel> {
        return storyPageDao.queryStoryPagesByChapterId(
            chapterId
        ).map { page ->
            StoryPageModel(
                characterModels = storyCharacterDao.queryStoryCharacterByPageId(page.id).map {
                    StoryPageModel.StoryCharacterModel(
                        indexInPageView = it.indexInPageView,
                        wife = Wife.values().find { wife -> wife.wifeName == it.wifeName }!!,
                        clothesIndex = it.clothesIndex,
                        expressionIndex = it.expressionIndex,
                        scale = it.scale,
                        translateX = it.translateX,
                        translateY = it.translateY,
                        id = it.id,
                        pageId = it.pageId
                    )
                } as ArrayList,
                header = page.header,
                content = page.content,
                background = page.background,
                id = page.id,
                chapterId = page.chapterId
            )
        }
    }

    suspend fun getLastStoryPageModelByChapterId(chapterId: Int): StoryPageModel? {
        return storyPageDao.queryStoryPagesByChapterId(
            chapterId
        ).lastOrNull()?.let { page ->
            StoryPageModel(
                characterModels = storyCharacterDao.queryStoryCharacterByPageId(page.id).map {
                    StoryPageModel.StoryCharacterModel(
                        indexInPageView = it.indexInPageView,
                        wife = Wife.values().find { wife -> wife.wifeName == it.wifeName }!!,
                        clothesIndex = it.clothesIndex,
                        expressionIndex = it.expressionIndex,
                        scale = it.scale,
                        translateX = it.translateX,
                        translateY = it.translateY,
                        id = it.id,
                        pageId = it.pageId
                    )
                } as ArrayList,
                header = page.header,
                content = page.content,
                background = page.background,
                id = page.id,
                chapterId = page.chapterId
            )
        }
    }

    suspend fun addStoryPageAndCharacterFromStoryPageModel(storyPageModel: StoryPageModel) {
        storyPageDao.insertStoryPage(
            NoIdStoryPage(
                chapterId = storyPageModel.chapterId,
                background = storyPageModel.background,
                content = storyPageModel.content,
                header = storyPageModel.header
            )
        )
        val storyPage = storyPageDao.queryStoryPagesByChapterId(storyPageModel.chapterId).last()
        storyCharacterDao.insertStoryCharacter(
            *storyPageModel.characterModels.map {
                NoIdStoryCharacter(
                    pageId = storyPage.id,
                    indexInPageView = it.indexInPageView,
                    wifeName = it.wife.wifeName,
                    clothesIndex = it.clothesIndex,
                    expressionIndex = it.expressionIndex,
                    scale = it.scale,
                    translateX = it.translateX,
                    translateY = it.translateY
                )
            }.toTypedArray()
        )
    }

    suspend fun updateStoryPage(storyPageModel: StoryPageModel) {
        storyPageDao.updateStoryPage(
            StoryPage(
                id = storyPageModel.id,
                chapterId = storyPageModel.chapterId,
                background = storyPageModel.background,
                content = storyPageModel.content,
                header = storyPageModel.header
            )
        )
    }

    suspend fun insertCharacter(characterModel: StoryPageModel.StoryCharacterModel) {
        storyCharacterDao.insertStoryCharacter(
            characterModel.let {
                NoIdStoryCharacter(
                    pageId = it.pageId,
                    indexInPageView = it.indexInPageView,
                    wifeName = it.wife.wifeName,
                    clothesIndex = it.clothesIndex,
                    expressionIndex = it.expressionIndex,
                    scale = it.scale,
                    translateX = it.translateX,
                    translateY = it.translateY
                )
            }
        )
    }

    suspend fun deleteCharacter(characterModel: StoryPageModel.StoryCharacterModel) {
        storyCharacterDao.deleteStoryCharacter(
            characterModel.let {
                StoryCharacter(
                    id = it.id,
                    pageId = it.pageId,
                    indexInPageView = it.indexInPageView,
                    wifeName = it.wife.wifeName,
                    clothesIndex = it.clothesIndex,
                    expressionIndex = it.expressionIndex,
                    scale = it.scale,
                    translateX = it.translateX,
                    translateY = it.translateY
                )
            }
        )
    }

    suspend fun updateCharacter(characterModel: StoryPageModel.StoryCharacterModel) {
        storyCharacterDao.updateStoryCharacter(
            characterModel.let {
                StoryCharacter(
                    id = it.id,
                    pageId = it.pageId,
                    indexInPageView = it.indexInPageView,
                    wifeName = it.wife.wifeName,
                    clothesIndex = it.clothesIndex,
                    expressionIndex = it.expressionIndex,
                    scale = it.scale,
                    translateX = it.translateX,
                    translateY = it.translateY
                )
            }
        )
    }

    suspend fun deleteStoryPageById(pageId: Int) {
        storyPageDao.queryStoryPageById(pageId)?.let {
            storyPageDao.deleteStoryPage(it)
        }
    }

    suspend fun getPreviousEditRecordByStoryId(storyId: Int) = previousEditRecordDao.queryPreviousEditRecordByStoryId(storyId)

    suspend fun insertPreviousEditRecord(noIdPreviousEditRecord: NoIdPreviousEditRecord) {
        previousEditRecordDao.insertPreviousEditRecord(noIdPreviousEditRecord)
    }

    suspend fun updatePreviousEditRecord(previousEditRecord: PreviousEditRecord) {
        previousEditRecordDao.updatePreviousEditRecord(previousEditRecord)
    }

    companion object {

        fun create(context: Context): StoryRepository {
            return YuzuSoftAppWidgetDatabase.getDatabase(context).run {
                StoryRepository(
                    storyDao(),
                    storyChapterDao(),
                    storyPageDao(),
                    storyCharacterDao(),
                    storyRecordDataDao(),
                    previousEditRecordDao()
                )
            }
        }

    }
}