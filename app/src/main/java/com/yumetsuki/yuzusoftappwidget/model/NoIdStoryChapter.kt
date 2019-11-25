package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo

class NoIdStoryChapter(
    @ColumnInfo(name = "story_id")
    val storyId: Int,

    @ColumnInfo(name = "chapter_name")
    val chapterName: String
)