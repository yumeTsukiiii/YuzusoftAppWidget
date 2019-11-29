package com.yumetsuki.yuzusoftappwidget.page.story.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.StartRecordMode
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryActViewModel
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryStarterViewModel
import com.yumetsuki.yuzusoftappwidget.page.story_data.StoryRecordDataActivity
import com.yumetsuki.yuzusoftappwidget.utils.toast
import kotlinx.android.synthetic.main.fragment_story_starter.view.*

class StoryStarterFragment : Fragment() {

    companion object {
        const val STORY_RECORD_PAGE_REQUEST_CODE = 3001
        fun newInstance() = StoryStarterFragment()
    }

    private val viewModel: StoryStarterViewModel by lazy {
        ViewModelProviders.of(this).get(StoryStarterViewModel::class.java)
    }

    private val actViewModel: StoryActViewModel by lazy {
        ViewModelProviders.of(this.activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application))
            .get(StoryActViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_starter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            mStartNewStoryBtn.setOnClickListener {
                actViewModel.requestFirstChapterId()
            }
            mStartStoryFromRecordBtn.setOnClickListener {
                startActivityForResult(
                    Intent(this@StoryStarterFragment.activity!!, StoryRecordDataActivity::class.java)
                        .apply {
                            putExtra(StoryRecordDataActivity.STORY_START_MODE_EXTRA, StartRecordMode.LOAD)
                            putExtra(StoryRecordDataActivity.STORY_ID_EXTRA, actViewModel.story.value!!.id)
                        },
                    STORY_RECORD_PAGE_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.takeIf {
            requestCode == STORY_RECORD_PAGE_REQUEST_CODE
                    && resultCode == StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_CODE
        }?.also { backData ->
            val pageId = backData.getIntExtra(StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_PAGE_ID_EXTRA, -1)
            val chapterId = backData.getIntExtra(StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_CHAPTER_ID_EXTRA, -1)
            if (pageId == -1 || chapterId == -1) {
                toast("出现了蜜汁错误")
                return
            } else {
                actViewModel.jumpToChapterPage(pageId, pageId)
            }
        }
    }

}
