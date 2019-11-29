package com.yumetsuki.yuzusoftappwidget.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryPage

class NoIdStoryRecordData(

    @ColumnInfo(name = "story_id")
    @ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["story_id"], onDelete = ForeignKey.CASCADE)
    val storyId: Int,

    @ColumnInfo(name = "page_id")
    @ForeignKey(entity = StoryPage::class, parentColumns = ["id"], childColumns = ["page_id"], onDelete = ForeignKey.CASCADE)
    val pageId: Int
)