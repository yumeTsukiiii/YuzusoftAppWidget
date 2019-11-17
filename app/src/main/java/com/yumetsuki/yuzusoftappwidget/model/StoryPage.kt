package com.yumetsuki.yuzusoftappwidget.model

import com.yumetsuki.yuzusoftappwidget.config.Wife

data class StoryPage(
    val characters: ArrayList<StoryCharacter>,
    val text: String
)

data class StoryCharacter(
    val indexInPageView: Int,
    val wife: Wife,
    val clothesIndex: Int,
    val expressionIndex: Int
)