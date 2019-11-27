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

    fun requestFirstChapterId() {
        viewModelScope.launch {
            currentChapterId.value = withContext(Dispatchers.IO) {
                storyRepository.getChaptersByStoryId(story.value!!.id).lastOrNull()?.id
            }
        }
    }

}