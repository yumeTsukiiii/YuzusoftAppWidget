package com.yumetsuki.yuzusoftappwidget.page.story.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryActViewModel(
    application: Application
): AndroidViewModel(application) {

    val story = MutableLiveData<Story>()

    private val storyRepository = StoryRepository.create(application)

    val currentChapterId = MutableLiveData<Int>()

    val toastTip = MutableLiveData<String>()

    var currentPageId = -1
        private set

    fun requestFirstChapterId() {
        viewModelScope.launch {
            currentChapterId.value = withContext(Dispatchers.IO) {
                storyRepository.getChaptersByStoryId(story.value!!.id).firstOrNull()?.id
            }
        }
    }

    fun nextChapter() {
        viewModelScope.launch {
            currentChapterId.value = withContext(Dispatchers.IO) {
                storyRepository.getChaptersByStoryId(story.value!!.id).run {
                    val currentIndex = indexOfFirst {
                        it.id == currentChapterId.value!!
                    }
                    if (currentIndex == -1 || currentIndex + 1 >= this.size) {
                        null
                    } else {
                        currentPageId = -1
                        toastTip.postValue("章节:${this[currentIndex].chapterName}")
                        this[currentIndex + 1].id
                    }
                }
            }
        }
    }

    fun jumpToChapterPage(chapterId: Int, pageId: Int) {
        currentPageId = pageId
        currentChapterId.value = chapterId
    }

}