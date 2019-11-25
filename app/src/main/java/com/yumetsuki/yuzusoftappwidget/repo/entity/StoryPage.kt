package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "story_page")
class StoryPage(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ForeignKey(entity = StoryChapter::class, parentColumns = ["id"], childColumns = ["chapter_id"], onDelete = CASCADE)
    @ColumnInfo(name = "chapter_id")
    val chapterId: Int,

    @ColumnInfo(name = "background")
    val background: Int,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "header")
    val header: String?

)