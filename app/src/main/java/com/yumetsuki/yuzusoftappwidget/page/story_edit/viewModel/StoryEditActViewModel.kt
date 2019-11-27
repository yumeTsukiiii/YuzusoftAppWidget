package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.NoIdPreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.PreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryChapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryEditActViewModel(
    application: Application
): AndroidViewModel(application) {

    private val storyRepository = StoryRepository.create(application)

    val currentChapterId = MutableLiveData<Int>()

    val previousEditRecord = MutableLiveData<PreviousEditRecord>()

    val story = MutableLiveData<Story>()

    fun requestPreviousEditRecord(storyId: Int) {
        viewModelScope.launch {
            previousEditRecord.value = withContext(Dispatchers.IO) {
                storyRepository.getPreviousEditRecordByStoryId(
                    storyId
                )?:storyRepository.insertPreviousEditRecord(NoIdPreviousEditRecord(
                    storyId, -1, -1
                )).run {
                    storyRepository.getPreviousEditRecordByStoryId(storyId)
                }
            }
        }
    }

    fun updatePreviousEditRecord(chapterId: Int, pageId: Int) {
        viewModelScope.launch {
            previousEditRecord.value = withContext(Dispatchers.IO) {
                storyRepository.updatePreviousEditRecord(
                    PreviousEditRecord(
                        previousEditRecord.value!!.id,
                        story.value!!.id,
                        chapterId,
                        pageId
                    )
                )
                storyRepository.getPreviousEditRecordByStoryId(story.value!!.id)
            }
        }
    }



}