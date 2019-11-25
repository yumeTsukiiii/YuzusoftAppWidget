package com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story_edit.adapter.ChapterEditItemAdapter
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryChapterFragViewModel
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditActViewModel
import kotlinx.android.synthetic.main.dialog_add_chapter.view.*
import kotlinx.android.synthetic.main.fragment_story_chapter_edit.view.*
import kotlinx.android.synthetic.main.fragment_story_chapter_edit.view.mChapterEditRecyclerView

class StoryChapterEditFragment: Fragment() {

    private val actViewModel: StoryEditActViewModel by lazy {
        ViewModelProvider(this.activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryEditActViewModel::class.java)
    }

    private val viewModel: StoryChapterFragViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryChapterFragViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.requestChapters(actViewModel.story.value!!.id)

        viewModel.chapters.observe(this, Observer {
            view?.apply {
                mNoChapterTv.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                mChapterEditRecyclerView.adapter?.notifyDataSetChanged()
            }
        })

        viewModel.isDrawerExpand.observe(this, Observer {
            it?.also { isDrawerExpand ->
                view?.apply {
                    if (isDrawerExpand) {
                        ObjectAnimator.ofFloat(mRightToolDrawer, "translationX" , 0f, mRightToolDrawerContent.width.toFloat())
                            .start()
                    } else {
                        ObjectAnimator.ofFloat(mRightToolDrawer, "translationX" , mRightToolDrawerContent.width.toFloat(), 0f)
                            .start()
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_chapter_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            mChapterEditRecyclerView.layoutManager = LinearLayoutManager(this@StoryChapterEditFragment.context)
            mChapterEditRecyclerView.adapter = ChapterEditItemAdapter(
                viewModel.chapters,
                {

                }
            ) { chapter, _ ->
                actViewModel.currentChapterId.value = chapter.id
            }
            mToggleRightToolDrawerImg.setOnClickListener {
                viewModel.isDrawerExpand.value = !(viewModel.isDrawerExpand.value?:false)
            }
            mAddChapterBtn.setOnClickListener {
                buildAddChapterDialog().show()
            }
            mExitChapterEditBtn.setOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun buildAddChapterDialog() = AlertDialog.Builder(this.context!!).apply {
        View.inflate(this@StoryChapterEditFragment.context, R.layout.dialog_add_chapter, null).apply {
            mChapterNameEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    mChapterNameEditText.error = if (s == null || s.toString().isEmpty()) {
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

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
            setTitle("添加章节").setView(
                this
            ).setPositiveButton("添加章节") { dialog, _ ->
                if (!(mChapterNameEditText.text == null || mChapterNameEditText.text.toString().isEmpty()))  {
                    viewModel.addChapter(actViewModel.story.value!!.id, mChapterNameEditText.text.toString())
                    dialog.cancel()
                }
            }.setNegativeButton("取消") { dialog, _ ->
                dialog.cancel()
            }
        }
    }.create()

    companion object {

        fun newInstance(): StoryChapterEditFragment {
            return StoryChapterEditFragment()
        }

    }

}