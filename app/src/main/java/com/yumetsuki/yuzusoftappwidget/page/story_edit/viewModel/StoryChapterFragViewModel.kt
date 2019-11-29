package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryChapter
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryChapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryChapterFragViewModel(
    application: Application
): AndroidViewModel(application) {

    private val storyRepository = StoryRepository.create(application)

    val isDrawerExpand = MutableLiveData<Boolean>()

    val chapters = MutableLiveData<ArrayList<StoryChapter>>()

    fun requestChapters(storyId: Int) {
        viewModelScope.launch {
            chapters.value = withContext(Dispatchers.IO) {
                storyRepository.getChaptersByStoryId(storyId) as ArrayList<StoryChapter>
            }
        }
    }

    fun addChapter(storyId: Int, chapterName: String) {
        viewModelScope.launch {
            chapters.value = withContext(Dispatchers.IO) {
                storyRepository.addChapter(NoIdStoryChapter(storyId, chapterName))
                storyRepository.getChaptersByStoryId(storyId) as ArrayList<StoryChapter>
            }
        }
    }

    fun removeChapter(chapter: StoryChapter) {
        viewModelScope.launch {
            chapters.value = withContext(Dispatchers.IO) {
                storyRepository.deleteChapter(chapter)
                storyRepository.getChaptersByStoryId(chapter.storyId) as ArrayList
            }
        }
    }

}