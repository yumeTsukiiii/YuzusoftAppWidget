package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "story_chapter")
class StoryChapter(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ForeignKey(entity = Story::class, parentColumns = ["id"], childColumns = ["story_id"], onDelete = CASCADE)
    @ColumnInfo(name = "story_id")
    val storyId: Int,

    @ColumnInfo(name = "chapter_name")
    val chapterName: String

)