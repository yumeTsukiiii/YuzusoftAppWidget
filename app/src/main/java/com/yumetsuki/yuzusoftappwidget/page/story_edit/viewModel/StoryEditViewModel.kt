package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.StoryCharacter
import com.yumetsuki.yuzusoftappwidget.model.StoryPage

class StoryEditViewModel: ViewModel() {

    val isDrawerExpand = MutableLiveData<Boolean>()

    val currentStoryPage = MutableLiveData<Int>()

    val storyPages = MutableLiveData<ArrayList<StoryPage>>()

    fun addCharacterClothes(wife: Wife, indexInPageView: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characters.add(
                StoryCharacter(
                    indexInPageView, wife, 0, 0
                )
            )
            storyPages.postValue(this)
        }
    }

    fun changeCharacterClothes(index: Int, clothesIndex: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characters[index] = this[currentStoryPage.value!!].characters[index].copy(clothesIndex = clothesIndex)
            storyPages.postValue(this)
        }
    }

    fun changeCharacterExpression(index: Int, expressionIndex: Int) {
        storyPages.value?.takeIf {
            currentStoryPage.value != null
        }?.apply {
            this[currentStoryPage.value!!].characters[index] = this[currentStoryPage.value!!].characters[index].copy(expressionIndex = expressionIndex)
            storyPages.postValue(this)
        }
    }

}