package com.yumetsuki.yuzusoftappwidget.page.story_data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.StartRecordMode
import com.yumetsuki.yuzusoftappwidget.page.story_data.adapter.StoryRecordDataItemAdapter
import com.yumetsuki.yuzusoftappwidget.page.story_data.viewmodel.StoryRecordDataViewModel
import com.yumetsuki.yuzusoftappwidget.utils.toast
import kotlinx.android.synthetic.main.activity_story_record_data.*

class StoryRecordDataActivity : AppCompatActivity() {

    companion object {
        const val STORY_RECORD_PAGE_RESULT_CODE = 4001
        const val STORY_RECORD_PAGE_RESULT_PAGE_ID_EXTRA = "story_record_page_result_page_id_extra"
        const val STORY_RECORD_PAGE_RESULT_CHAPTER_ID_EXTRA = "story_record_page_result_chapter_id_extra"

        const val STORY_START_MODE_EXTRA = "story_start_mode_extra"
        const val STORY_ID_EXTRA = "story_id_extra"
        const val PAGE_ID_EXTRA = "page_id_extra"
    }

    private val viewModel: StoryRecordDataViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(StoryRecordDataViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_record_data)

        @Suppress("UNCHECKED_CAST")
        viewModel.mode = intent.getSerializableExtra(STORY_START_MODE_EXTRA) as StartRecordMode
        if (viewModel.mode == StartRecordMode.SAVE) {
            viewModel.pageId = intent.getIntExtra(PAGE_ID_EXTRA, -1)
            if (viewModel.pageId == -1) {
                toast("没有这个id啦")
                finish()
            }
        }

        setSupportActionBar(mStoryRecordDataToolbar.apply {
            title = when(viewModel.mode) {
                StartRecordMode.SAVE -> {
                    "存档"
                }
                StartRecordMode.LOAD -> {
                    "读取存档"
                }
            }
        })

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        mStoryRecordDataToolbar.setNavigationOnClickListener {
            finish()
        }

        intent.getIntExtra(STORY_ID_EXTRA, -1).takeIf {
            it != -1
        }?.let {
            viewModel.requestStoryPageModelsFromRecord(it)
        }?:run {
            toast("没有这个id啦")
            finish()
        }

        mStoryRecordDataRecyclerView.layoutManager = GridLayoutManager(this, 3)

        mStoryRecordDataRecyclerView.adapter = StoryRecordDataItemAdapter(
            viewModel.storyPageViewModels,
            { _, index ->
                if (viewModel.currentRecordIndex.value != index) {
                    viewModel.currentRecordIndex.value = index
                } else {
                    when(viewModel.mode) {
                        StartRecordMode.SAVE -> {
                            coverSaveData()
                        }
                        StartRecordMode.LOAD -> {
                            confirmLoadData()
                        }
                    }
                }
            }
        ) { storyPageModel, _ ->
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除该存档吗？")
                .setPositiveButton("确定") { dialog, _ ->
                    viewModel.removeRecord(storyPageModel)
                    dialog.cancel()
                }.setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }.create().show()
        }

        viewModel.storyPageViewModels.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                mNoSaveRecordTipTv.visibility = View.VISIBLE
                mStoryRecordDataContentLayout.visibility = View.GONE
                return@Observer
            } else {
                mNoSaveRecordTipTv.visibility = View.GONE
                mStoryRecordDataContentLayout.visibility = View.VISIBLE
            }
            if (viewModel.currentRecordIndex.value?:-1 >= 0) {
                mStoryRecordSpeakerNameTv.text = viewModel.storyPageViewModels.value!![viewModel.currentRecordIndex.value!!].second.header?:""
                mStoryRecordSpeakerContentTv.text = viewModel.storyPageViewModels.value!![viewModel.currentRecordIndex.value!!].second.content
            }
            mStoryRecordDataRecyclerView.adapter?.notifyDataSetChanged()
        })

        viewModel.currentRecordIndex.observe(this, Observer {
            mStoryRecordSpeakerNameTv.text = viewModel.storyPageViewModels.value!![it].second.header?:""
            mStoryRecordSpeakerNameTv.visibility = if (viewModel.storyPageViewModels.value!![it].second.header.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            mStoryRecordSpeakerContentTv.text = viewModel.storyPageViewModels.value!![it].second.content
            viewModel.refreshCurrentModel()
        })

        viewModel.toastTip.observe(this, Observer {
            toast(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (viewModel.mode == StartRecordMode.SAVE) {
            menuInflater.inflate(R.menu.activity_story_record_data_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_save_data -> {
                viewModel.saveData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun coverSaveData() {
        AlertDialog.Builder(this).setTitle("提示")
            .setMessage("确定覆盖该存档吗？")
            .setNegativeButton("取消") { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton("确定") { dialog, _ ->
                viewModel.coverSaveData()
                dialog.cancel()
            }.create().show()
    }

    private fun confirmLoadData() {
        AlertDialog.Builder(this).setTitle("提示")
            .setMessage("确定读取该存档吗？")
            .setNegativeButton("取消") { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton("确定") { dialog, _ ->
                setResult(STORY_RECORD_PAGE_RESULT_CODE, Intent().apply {
                    putExtra(
                        STORY_RECORD_PAGE_RESULT_PAGE_ID_EXTRA,
                        viewModel.storyPageViewModels.value!![viewModel.currentRecordIndex.value!!].second.id
                    )
                    putExtra(
                        STORY_RECORD_PAGE_RESULT_CHAPTER_ID_EXTRA,
                        viewModel.storyPageViewModels.value!![viewModel.currentRecordIndex.value!!].second.chapterId
                    )
                })
                dialog.cancel()
                finish()
            }.create().show()
    }
}
