package com.yumetsuki.yuzusoftappwidget.page.story_edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.PreviousEditRecord
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments.StoryChapterEditFragment
import com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments.StoryEditFragment
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditActViewModel
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story

class StoryEditActivity: AppCompatActivity() {

    private val viewModel: StoryEditActViewModel by lazy {
        ViewModelProvider(this).get(StoryEditActViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_edit)

        viewModel.story.value = intent.getParcelableExtra<Story>(STORY_EXTRA)

        viewModel.story.observe(this, Observer {
            navigateToChapterOrPageEdit(viewModel.getPreviousEditRecord())
        })

        viewModel.currentChapterId.observe(this, Observer {
            it?.also { _ ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StoryEditFragment.newInstance())
                    .commitNow()
            }?:supportFragmentManager.beginTransaction()
                .replace(R.id.container, StoryChapterEditFragment.newInstance())
                .commitNow()
        })

    }

    private fun navigateToChapterOrPageEdit(previousEditRecord: PreviousEditRecord) {
        if (previousEditRecord.chapterId > 0) {
            viewModel.currentChapterId.value = previousEditRecord.chapterId
        } else {
            viewModel.currentChapterId.value = null
        }
    }

    companion object {
        const val STORY_EXTRA = "edit_story"
    }
}