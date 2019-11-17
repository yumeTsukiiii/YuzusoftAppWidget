package com.yumetsuki.yuzusoftappwidget.page.story_edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments.StoryEditFragment

class StoryEditActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_edit)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, StoryEditFragment.newInstance())
            .commit()

    }
}