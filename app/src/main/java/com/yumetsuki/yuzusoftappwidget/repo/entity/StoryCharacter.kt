package com.yumetsuki.yuzusoftappwidget.repo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "story_character")
class StoryCharacter(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ForeignKey(entity = StoryPage::class, parentColumns = ["id"], childColumns = ["page_id"], onDelete = CASCADE)
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