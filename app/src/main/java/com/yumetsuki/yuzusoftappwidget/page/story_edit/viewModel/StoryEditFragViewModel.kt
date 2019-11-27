package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yumetsuki.yuzusoftappwidget.config.Background
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.SimpleHistory
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel
import com.yumetsuki.yuzusoftappwidget.repo.StoryRepository
import com.yumetsuki.yuzusoftappwidget.repo.entity.PreviousEditRecord
import kotlinx.coroutines.*

class StoryEditFragViewModel(
    application: Application
) : AndroidViewModel(application) {

    val toastTip = MutableLiveData<String>()

    val isDrawerExpand = MutableLiveData<Boolean>()

    val currentStoryPage = MutableLiveData<StoryPageModel>()

    val isShowHistory = MutableLiveData<Boolean>(false)

    val histories = MutableLiveData<List<SimpleHistory>>()

    private val removedCharacter = arrayListOf<StoryPageModel.StoryCharacterModel>()

    private val storyRepository = StoryRepository.create(application)

    fun requestStoryPageByRecordPageId(chapterId: Int, pageId: Int) {
        viewModelScope.launch {
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelByPageId(pageId) ?: StoryPageModel(
                    arrayListOf(),
                    null,
                    "",
                    Background.JinjiaBack.res,
                    chapterId = chapterId
                )
            }
        }
    }

    fun requestLastStoryPageModelByChapterId(chapterId: Int) {
        viewModelScope.launch {
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getLastStoryPageModelByChapterId(chapterId) ?: StoryPageModel(
                    arrayListOf(),
                    null,
                    "",
                    Background.JinjiaBack.res,
                    chapterId = chapterId
                )
            }
        }
    }

    fun addCharacterClothes(wife: Wife, indexInPageView: Int) {
        currentStoryPage.value?.apply {
            characterModels.add(
                StoryPageModel.StoryCharacterModel(
                    indexInPageView, wife, 0, 0, 1f, 0f, 0f,
                    pageId = currentStoryPage.value!!.id
                )
            )
            currentStoryPage.postValue(this)
        }
    }

    fun removeCharacterClothes(characterModel: StoryPageModel.StoryCharacterModel) {
        currentStoryPage.value?.apply {
            this.characterModels.remove(characterModel)
            currentStoryPage.postValue(this)
        }
        removedCharacter.add(characterModel)
    }

    fun changeCharacterClothes(index: Int, clothesIndex: Int) {
        currentStoryPage.value?.apply {
            characterModels[index] = characterModels[index].copy(clothesIndex = clothesIndex)
            currentStoryPage.postValue(this)
        }
    }

    fun changeCharacterExpression(index: Int, expressionIndex: Int) {
        currentStoryPage.value?.apply {
            characterModels[index] = characterModels[index].copy(expressionIndex = expressionIndex)
            currentStoryPage.postValue(this)
        }
    }

    fun changeCharacterTranslate(index: Int, translateX: Float, translateY: Float) {
        currentStoryPage.value?.apply {
            characterModels[index] =
                characterModels[index].copy(translateX = translateX, translateY = translateY)
            currentStoryPage.postValue(this)
        }
    }

    fun changeCharacterScale(index: Int, scale: Float) {
        currentStoryPage.value?.apply {
            characterModels[index] = characterModels[index].copy(scale = scale)
            currentStoryPage.postValue(this)
        }
    }

    fun updateBackground(background: Background) {
        currentStoryPage.value?.apply {
            currentStoryPage.postValue(this.copy(background = background.res))
        }
    }

    fun updatePageModelHeader(header: String?) {
        currentStoryPage.value?.apply {
            currentStoryPage.postValue(this.copy(header = header))
        }
    }

    fun updatePageModelContent(content: String) {
        currentStoryPage.value?.apply {
            currentStoryPage.postValue(this.copy(content = content))
        }
    }

    fun saveCurrentPage(): Job {
        return viewModelScope.launch {
            if (currentStoryPage.value!!.id == -1) {
                withContext(Dispatchers.IO) {
                    storyRepository.addStoryPageAndCharacterFromStoryPageModel(currentStoryPage.value!!)
                }
                currentStoryPage.value =
                    storyRepository.getLastStoryPageModelByChapterId(currentStoryPage.value!!.chapterId)
            } else {
                withContext(Dispatchers.IO) {
                    storyRepository.updateStoryPage(currentStoryPage.value!!)
                    currentStoryPage.value!!.characterModels.forEach {
                        if (it.id == -1) {
                            storyRepository.insertCharacter(it)
                        } else {
                            storyRepository.updateCharacter(it)
                        }
                    }
                }
            }
            removedCharacter.forEach {
                if (it.id != -1) {
                    storyRepository.deleteCharacter(it)
                }
            }
            removedCharacter.clear()
            toastTip.postValue("保存成功")
        }
    }

    fun requestAllHistories() {
        viewModelScope.launch {
            histories.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPagesByChapterId(
                    currentStoryPage.value!!.chapterId
                ).map {
                    SimpleHistory(it.id, it.header?:"", it.content)
                }.apply {
                    println(this)
                }
            }
        }
    }

    fun jumpCurrentPage(pageId: Int) {
        viewModelScope.launch {
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelByPageId(pageId)
            }
        }
    }

    fun removePage(pageId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                storyRepository.deleteStoryPageById(pageId)
            }
            if (pageId == currentStoryPage.value!!.id) {
                currentStoryPage.value = withContext(Dispatchers.IO) {
                    storyRepository.getLastStoryPageModelByChapterId(
                        currentStoryPage.value!!.chapterId
                    )?:StoryPageModel(
                        arrayListOf(),
                        null,
                        "",
                        Background.JinjiaBack.res,
                        chapterId = currentStoryPage.value!!.chapterId
                    )
                }
            }
            toastTip.postValue("删除成功")
        }
    }

    fun nextPage(): Job {
        return viewModelScope.launch {
            saveCurrentPage().join()
            currentStoryPage.value = withContext(Dispatchers.IO) {
                storyRepository.getStoryPageModelsByChapterId(currentStoryPage.value!!.chapterId)
                    .run {
                        val currentIndex = indexOfFirst {
                            it.id == currentStoryPage.value!!.id
                        }
                        if (currentIndex == -1 || currentIndex + 1 >= this.size) {
                            currentStoryPage.value!!.copy(id = -1)
                        } else {
                            this[currentIndex + 1]
                        }
                    }
            }
            toastTip.postValue("已跳转")
        }
    }

}