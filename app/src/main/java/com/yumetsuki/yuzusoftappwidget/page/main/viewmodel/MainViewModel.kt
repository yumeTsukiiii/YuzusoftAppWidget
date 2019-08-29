package com.yumetsuki.yuzusoftappwidget.page.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val currentGameIndex = MutableLiveData<Int>()

}