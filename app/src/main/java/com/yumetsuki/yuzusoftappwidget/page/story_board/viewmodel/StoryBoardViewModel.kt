package com.yumetsuki.yuzusoftappwidget.page.story_board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.NoIdStory
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryBoardViewModel(
    application: Application
): AndroidViewModel(application) {

    private val storyRepository = StoryRepository.create(application)

    val isEditMode = MutableLiveData(false)

    val stories = MutableLiveData<ArrayList<Story>>()

    fun requestStories() {
        viewModelScope.launch {
            stories.value = withContext(Dispatchers.IO) {
                storyRepository.getStories() as ArrayList
            }
        }
    }

    fun addStory(storyName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.addStory(NoIdStory(storyName))
                stories.postValue(
                    storyRepository.getStories() as ArrayList<Story>
                )
            }
        }
    }

    fun removeStory(story: Story) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.removeStory(story)
                stories.postValue(
                    storyRepository.getStories() as ArrayList<Story>
                )
            }
        }
    }

}