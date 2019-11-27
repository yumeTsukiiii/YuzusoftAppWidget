package com.yumetsuki.yuzusoftappwidget.page.story_edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments.StoryChapterEditFragment
import com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments.StoryEditFragment
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditActViewModel
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story

class StoryEditActivity: AppCompatActivity() {

    private val viewModel: StoryEditActViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(StoryEditActViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_edit)

        viewModel.story.value = intent.getParcelableExtra<Story>(STORY_EXTRA)

        viewModel.story.observe(this, Observer {
            viewModel.requestPreviousEditRecord(it.id)
        })

        viewModel.previousEditRecord.observe(this, Observer {
            if (viewModel.currentChapterId.value == null) {
                viewModel.currentChapterId.value = it?.chapterId
            }
        })

        viewModel.currentChapterId.observe(this, Observer {
            it?.takeIf {
                it != -1
            }?.also { _ ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StoryEditFragment.newInstance())
                    .commitNow()
            }?:supportFragmentManager.beginTransaction()
                .replace(R.id.container, StoryChapterEditFragment.newInstance())
                .commitNow()
        })

    }

    companion object {
        const val STORY_EXTRA = "edit_story"
    }
}