package com.yumetsuki.yuzusoftappwidget.page.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story.fragments.StoryFragment
import com.yumetsuki.yuzusoftappwidget.page.story.fragments.StoryStarterFragment
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryActViewModel
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import com.yumetsuki.yuzusoftappwidget.utils.toast

class StoryActivity : AppCompatActivity() {

    private val viewModel: StoryActViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(StoryActViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        viewModel.story.value = intent.getParcelableExtra<Story>(STORY_EXTRA)

        viewModel.currentChapterId.observe(this, Observer {
            it?.also { chapterId ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StoryFragment.newInstance(chapterId))
                    .commitNow()
            }?:run {
                toast("还没有章节啦！")
                finish()
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StoryStarterFragment.newInstance())
                .commitNow()
        }
    }

    companion object {
        const val STORY_EXTRA = "story"
    }

}
