package com.yumetsuki.yuzusoftappwidget.page.story_data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.model.NoIdStoryRecordData
import com.yumetsuki.yuzusoftappwidget.model.StartRecordMode
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryRecordData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryRecordDataViewModel(
    application: Application
) : AndroidViewModel(application) {

    var mode = StartRecordMode.LOAD

    val storyPageViewModels = MutableLiveData<ArrayList<Triple<Int, StoryPageModel, Boolean>>>() //first: recordId

    val currentRecordIndex = MutableLiveData<Int>()

    val toastTip = MutableLiveData<String>()

    var storyId: Int = -1

    var pageId: Int = -1

    private val storyRepository = StoryRepository.create(application)

    fun requestStoryPageModelsFromRecord(storyId: Int): Job {
        this.storyId = storyId
        return viewModelScope.launch {
            storyPageViewModels.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryRecordsByStoryId(storyId)
                    .map {
                        it.id to storyRepository.getStoryPageModelByPageId(it.pageId)!!
                    }.map {
                        Triple(it.first, it.second, false)
                    } as ArrayList
            }
        }
    }

    fun coverSaveData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.updateStoryRecord(
                    storyPageViewModels.value!![currentRecordIndex.value!!].run {
                        StoryRecordData(
                            first,
                            storyId,
                            pageId
                        )
                    }
                )
            }
            requestStoryPageModelsFromRecord(storyId).join()
            toastTip.value = "保存成功"
        }
    }

    fun saveData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.insertStoryRecord(
                    NoIdStoryRecordData(
                        storyId,
                        pageId
                    )
                )
            }
            requestStoryPageModelsFromRecord(storyId).join()
            toastTip.value = "保存成功"
        }
    }

    fun refreshCurrentModel() {
        storyPageViewModels.value = storyPageViewModels.value?.mapIndexed { index, model ->
            Triple(model.first, model.second, index == currentRecordIndex.value)
        } as ArrayList
    }

    fun removeRecord(storyPageModel: StoryPageModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.deleteStoryRecord(
                    StoryRecordData(
                        storyPageViewModels.value!![currentRecordIndex.value!!].first,
                        storyId,
                        storyPageModel.id
                    )
                )
            }
            requestStoryPageModelsFromRecord(storyId).join()
            toastTip.postValue("删除成功")
        }
    }

}