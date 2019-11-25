package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumetsuki.yuzusoftappwidget.config.Background
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel

class StoryEditFragViewModel: ViewModel() {

    val isDrawerExpand = MutableLiveData<Boolean>()

    //这里指page的index
    val currentStoryPage = MutableLiveData<Int>()

    val storyPages = MutableLiveData<ArrayList<StoryPageModel>>()

    fun addCharacterClothes(wife: Wife, indexInPageView: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characterModels.add(
                StoryPageModel.StoryCharacterModel(
                    indexInPageView, wife, 0, 0, 1f, 0f, 0f
                )
            )
            storyPages.postValue(this)
        }
    }

    fun changeCharacterClothes(index: Int, clothesIndex: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characterModels[index] = this[currentStoryPage.value!!].characterModels[index].copy(clothesIndex = clothesIndex)
            storyPages.postValue(this)
        }
    }

    fun changeCharacterExpression(index: Int, expressionIndex: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characterModels[index] = this[currentStoryPage.value!!].characterModels[index].copy(expressionIndex = expressionIndex)
            storyPages.postValue(this)
        }
    }

    fun changeCharacterTranslate(index: Int, translateX: Float, translateY: Float) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characterModels[index] = this[currentStoryPage.value!!].characterModels[index].copy(translateX = translateX, translateY = translateY)
            storyPages.postValue(this)
        }
    }

    fun changeCharacterScale(index: Int, scale: Float) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characterModels[index] = this[currentStoryPage.value!!].characterModels[index].copy(scale = scale)
            storyPages.postValue(this)
        }
    }

    fun updateBackground(background: Background) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!] = this[currentStoryPage.value!!].copy(background = background.res)
            storyPages.postValue(this)
        }
    }
}