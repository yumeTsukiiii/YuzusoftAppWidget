package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryCharacter
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryPage

class NoIdPreviousEditRecord(

    @ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["story_id"], onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "story_id")
    val storyId: Int,

    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "page_id")
    val pageId: Int
)