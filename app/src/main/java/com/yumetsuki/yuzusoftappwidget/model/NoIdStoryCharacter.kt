package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo

class NoIdStoryCharacter(

    @ColumnInfo(name = "page_id")
    val pageId: Int,

    @ColumnInfo(name = "index_in_page_view")
    val indexInPageView: Int,

    @ColumnInfo(name = "wife_name")
    val wifeName: String,

    @ColumnInfo(name = "clothes_index")
    val clothesIndex: Int,

    @ColumnInfo(name = "expression_index")
    val expressionIndex: Int,

    @ColumnInfo(name = "scale")
    val scale: Float,

    @ColumnInfo(name = "translate_x")
    val translateX: Float,

    @ColumnInfo(name = "translate_y")
    val translateY: Float

)