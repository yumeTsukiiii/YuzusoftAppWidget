package com.yumetsuki.yuzusoftappwidget.page.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yumetsuki.yuzusoftappwidget.R

class StoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.story_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StoryFragment.newInstance())
                .commitNow()
        }
    }

    companion object {
        const val STORY_EXTRA = "story"
    }

}
