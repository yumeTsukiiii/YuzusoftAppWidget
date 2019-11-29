package com.yumetsuki.yuzusoftappwidget.page.story.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.SimpleHistory
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryFragViewModel(
    application: Application
): AndroidViewModel(application) {

    var storyId: Int = -1

    var previousStoryPage: StoryPageModel? = null
        private set

    val currentStoryPage = MutableLiveData<StoryPageModel>()

    val isShowHistory = MutableLiveData<Boolean>(false)

    val histories = MutableLiveData<List<SimpleHistory>>()

    private val storyRepository = StoryRepository.create(application)

    fun requestFirstStoryPage(chapterId: Int) {
        viewModelScope.launch {
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelsByChapterId(chapterId).firstOrNull()
            }
        }
    }

    fun requestPage(pageId: Int) {
        viewModelScope.launch {
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelByPageId(pageId)
            }
        }
    }

    fun nextPage(): Job {
        return viewModelScope.launch {

            previousStoryPage = currentStoryPage.value

            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelsByChapterId(currentStoryPage.value!!.chapterId)
                    .run {
                        val currentIndex = indexOfFirst {
                            it.id == currentStoryPage.value!!.id
                        }
                        if (currentIndex == -1 || currentIndex + 1 >= this.size) {
                            null
                        } else {
                            this[currentIndex + 1]
                        }
                    }
            }
        }
    }

    fun requestAllHistories() {
        viewModelScope.launch {
            histories.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPagesByChapterId(
                    currentStoryPage.value!!.chapterId
                ).map {
                    SimpleHistory(it.id, it.header?:"", it.content)
                }.run {
                    filterIndexed { index, _ ->
                        index <= indexOfFirst { it.pageId == currentStoryPage.value!!.id }
                    }
                }
            }
        }
    }

    fun jumpCurrentPage(pageId: Int) {
        viewModelScope.launch {
            previousStoryPage = null
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelByPageId(pageId)
            }
        }
    }

}
