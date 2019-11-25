package com.yumetsuki.yuzusoftappwidget.model

import com.yumetsuki.yuzusoftappwidget.config.Wife

data class StoryPageModel(
    val characterModels: ArrayList<StoryCharacterModel>,
    val header: String?,
    val content: String,
    val background: Int
) {

    data class StoryCharacterModel(
        val indexInPageView: Int,
        val wife: Wife,
        val clothesIndex: Int,
        val expressionIndex: Int,
        val scale: Float,
        val translateX: Float,
        val translateY: Float
    )

}