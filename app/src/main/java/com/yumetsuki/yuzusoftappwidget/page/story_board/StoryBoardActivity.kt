package com.yumetsuki.yuzusoftappwidget.page.story_board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story.StoryActivity
import com.yumetsuki.yuzusoftappwidget.page.story_board.adapter.StoryItemAdapter
import com.yumetsuki.yuzusoftappwidget.page.story_board.viewmodel.StoryBoardViewModel
import com.yumetsuki.yuzusoftappwidget.page.story_edit.StoryEditActivity
import kotlinx.android.synthetic.main.activity_story_board.*
import kotlinx.android.synthetic.main.dialog_add_story.view.*

class StoryBoardActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get<StoryBoardViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_board)
        setSupportActionBar(mStoryBoardToolbar)

        initViewModel()
        initView()
        viewModel.requestStories()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_story_board_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_edit_mode -> {
                viewModel.isEditMode.value = !(viewModel.isEditMode.value ?: false)
            }
            R.id.item_add_story -> {
                buildAddStoryDialog().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildAddStoryDialog() = AlertDialog.Builder(this).apply {
        View.inflate(this@StoryBoardActivity, R.layout.dialog_add_story, null).apply {
            mStoryNameEditText.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    mStoryNameEditText.error = if (s == null || s.toString().isEmpty()) {
                        "输入点东西啦。。"
                    } else {
                        null
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

            })
            setTitle("添加故事").setView(
                this
            ).setPositiveButton("添加") { dialog, _ ->
                if (!(mStoryNameEditText.text == null || mStoryNameEditText.text.toString().isEmpty())) {
                    viewModel.addStory(mStoryNameEditText.text.toString())
                    dialog.cancel()
                }
            }.setNegativeButton("取消") { dialog, _ ->
                dialog.cancel()
            }.create()
        }
    }


    private fun initView() {
        mStoryRecyclerView.layoutManager = GridLayoutManager(this, 3)

        mStoryRecyclerView.adapter = StoryItemAdapter(viewModel.stories, viewModel.isEditMode, { story ->
            AlertDialog.Builder(this).setTitle("提示")
                .setMessage("唔姆！确定要删除这个故事吗？！")
                .setPositiveButton("我不要了！") { dialog, _ ->
                    viewModel.removeStory(story)
                    dialog.dismiss()
                }.setNegativeButton("再拯救一下") { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }) {
            if (viewModel.isEditMode.value == true) {
                startActivity(
                    Intent(
                        this,
                        StoryEditActivity::class.java
                    ).apply {
                        putExtra(StoryEditActivity.STORY_EXTRA, it)
                    }
                )
            } else {
                startActivity(
                    Intent(
                        this,
                        StoryActivity::class.java
                    ).apply {
                        putExtra(StoryActivity.STORY_EXTRA, it)
                    }
                )
            }
        }
    }

    private fun initViewModel() {
        viewModel.stories.observe(this, Observer {
            mNoStoryTipLayout.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            mStoryRecyclerView.adapter?.notifyDataSetChanged()
        })

        viewModel.isEditMode.observe(this, Observer {
            mStoryRecyclerView.adapter?.notifyDataSetChanged()
            supportActionBar?.title = it?.let { if (!it) "故事板" else "故事板(编辑模式)" } ?: "故事板"
        })
    }
}
