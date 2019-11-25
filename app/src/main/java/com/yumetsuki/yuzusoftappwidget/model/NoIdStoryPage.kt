package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo

class NoIdStoryPage(

    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "background")
    val background: Int,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "header")
    val header: String?

)