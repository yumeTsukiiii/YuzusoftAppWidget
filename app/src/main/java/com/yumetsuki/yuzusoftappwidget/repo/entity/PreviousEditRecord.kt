package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "previous_edit_record")
class PreviousEditRecord(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["story_id"], onDelete = ForeignKey.CASCADE)
    @ColumnInfo(name = "story_id")
    val storyId: Int,

    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "page_id")
    val pageId: Int
)