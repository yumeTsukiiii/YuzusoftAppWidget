package com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumetsuki.yuzusoftappwidget.PreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryChapter

class StoryEditActViewModel: ViewModel() {

    val currentChapterId = MutableLiveData<Int>()

    val story = MutableLiveData<Story>()

    fun getPreviousEditRecord(): PreviousEditRecord {
        return PreviousEditRecord
    }



}