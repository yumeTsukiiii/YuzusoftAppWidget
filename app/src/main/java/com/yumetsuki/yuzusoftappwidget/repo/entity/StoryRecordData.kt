package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "story_record_data")
class StoryRecordData(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "story_id")
    @ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["story_id"], onDelete = ForeignKey.CASCADE)
    val storyId: Int,

    @ColumnInfo(name = "page_id")
    @ForeignKey(entity = StoryPage::class, parentColumns = ["id"], childColumns = ["page_id"], onDelete = ForeignKey.CASCADE)
    val pageId: Int
)