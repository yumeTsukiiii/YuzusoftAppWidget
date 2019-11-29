package com.yumetsuki.yuzusoftappwidget.page.story.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.StartRecordMode
import com.yumetsuki.yuzusoftappwidget.page.story.adapter.HistoryItemInStoryAdapter
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryActViewModel
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryFragViewModel
import com.yumetsuki.yuzusoftappwidget.page.story_data.StoryRecordDataActivity
import com.yumetsuki.yuzusoftappwidget.utils.toast
import com.yumetsuki.yuzusoftappwidget.widget.DragZoomLayout
import kotlinx.android.synthetic.main.fragment_story.view.*
import kotlinx.android.synthetic.main.widget_character.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryFragment : Fragment() {

    companion object {

        private const val STORY_RECORD_PAGE_REQUEST_CODE = 3001

        private const val CHAPTER_ID_ARGUMENT = "story_chapter_id_argument"

        private const val DATA_PAGE_ID_ARGUMENT = "story_page_id_argument"

        fun newInstance(chapterId: Int, pageId: Int = -1) = StoryFragment().apply {
            arguments = Bundle().apply {
                putInt(CHAPTER_ID_ARGUMENT, chapterId)
                putInt(DATA_PAGE_ID_ARGUMENT, pageId)
            }
        }
    }

    private val viewModel: StoryFragViewModel by lazy {
        ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryFragViewModel::class.java)
    }

    private val actViewModel: StoryActViewModel by lazy {
        ViewModelProvider(this.activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryActViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments!!.getInt(DATA_PAGE_ID_ARGUMENT).takeIf {
            it != -1
        }?.also {
            viewModel.requestPage(it)
        }?:viewModel.requestFirstStoryPage(
            arguments!!.getInt(CHAPTER_ID_ARGUMENT)
        )

        viewModel.isShowHistory.observe(this, Observer {
            view?.mHistoryLayout?.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.histories.observe(this, Observer {
            it?.also {
                view?.mHistoryRecyclerView?.adapter?.notifyDataSetChanged()
            }
        })

        viewModel.currentStoryPage.observe(this, Observer {
            it?.also { storyPageModel ->

                view?.apply { mStoryBackgroundImg.setImageResource(
                    storyPageModel.background
                )
                    if (mStorySpeakerNameTv.text != storyPageModel.header) {
                        mStorySpeakerNameTv.text = storyPageModel.header?:""
                    }
                    if (mStorySpeakerContentTv.text != storyPageModel.content) {
                        mStorySpeakerContentTv.text = storyPageModel.content
                    }
                }

                viewModel.previousStoryPage?.also { preStoryPageModel ->
                    animationRemoveRedundantCharacterView(view!!, storyPageModel.characterModels.map { model -> model.indexInPageView })
                    storyPageModel.characterModels.forEachIndexed { index, character ->
                        preStoryPageModel.characterModels.find { storyCharacterModel ->
                            storyCharacterModel.wife == character.wife
                        }?.also { preCharacterModel ->
                            if ((preCharacterModel.translateX - character.translateX) != 0f) {
                                animationGenerateCharacterView(
                                    view!!,
                                    character.wife,
                                    character.indexInPageView,
                                    index
                                )
                            } else {
                                generateCharacterView(view!!, character.wife, character.indexInPageView, index)
                            }
                        }?:generateCharacterView(view!!, character.wife, character.indexInPageView, index)
                    }
                }?:run {
                    removeRedundantCharacterView(view!!, storyPageModel.characterModels.map { model -> model.indexInPageView })
                    storyPageModel.characterModels.forEachIndexed { index, character ->
                        generateCharacterView(view!!, character.wife, character.indexInPageView, index)
                    }
                }
            }?:run {
                actViewModel.nextChapter()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            mHistoryRecyclerView.layoutManager = LinearLayoutManager(this@StoryFragment.context)
            mHistoryRecyclerView.adapter = HistoryItemInStoryAdapter(
                viewModel.histories
            ) { history, _ ->
                viewModel.jumpCurrentPage(history.pageId)
                viewModel.isShowHistory.value = false
            }
            mStoryContainer.setOnClickListener {
                viewModel.nextPage()
            }
            mStoryContentLayout.setOnClickListener {
                viewModel.nextPage()
            }
            mStorySaveBtn.setOnClickListener {
                startActivity(
                    Intent(this@StoryFragment.activity, StoryRecordDataActivity::class.java).apply {
                        putExtra(StoryRecordDataActivity.STORY_START_MODE_EXTRA, StartRecordMode.SAVE)
                        putExtra(StoryRecordDataActivity.STORY_ID_EXTRA, actViewModel.story.value!!.id)
                        putExtra(StoryRecordDataActivity.PAGE_ID_EXTRA, viewModel.currentStoryPage.value!!.id)
                    }
                )
            }
            mStoryLoadBtn.setOnClickListener {
                startActivityForResult(
                    Intent(
                        this@StoryFragment.activity, StoryRecordDataActivity::class.java
                    ).apply {
                        putExtra(StoryRecordDataActivity.STORY_START_MODE_EXTRA, StartRecordMode.LOAD)
                        putExtra(StoryRecordDataActivity.STORY_ID_EXTRA, actViewModel.story.value!!.id)
                    },
                    STORY_RECORD_PAGE_REQUEST_CODE
                )
            }
            mStoryHistoryBtn.setOnClickListener {
                viewModel.isShowHistory.value = true
                viewModel.requestAllHistories()
            }
            mCloseHistoryPageBtn.setOnClickListener {
                viewModel.isShowHistory.value = false
                viewModel.histories.value = null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.takeIf {
            requestCode == STORY_RECORD_PAGE_REQUEST_CODE
                    && resultCode == StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_CODE
        }?.let { recordData ->
            val chapterId = recordData.getIntExtra(StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_CHAPTER_ID_EXTRA, -1)
            val pageId = recordData.getIntExtra(StoryRecordDataActivity.STORY_RECORD_PAGE_RESULT_PAGE_ID_EXTRA, -1)
            if (pageId == -1 || chapterId == -1) {
                toast("出现了蜜汁错误")
                return
            } else {
                actViewModel.jumpToChapterPage(pageId, pageId)
            }
        }
    }

    private fun animationRemoveRedundantCharacterView(parent: View, indexesInPage: List<Int>) {
        parent.mStoryContainer.children.filterIndexed { index, view ->
            view.findViewById<FrameLayout>(R.id.mCharacterLayout) != null && index !in indexesInPage
        }.map { characterView ->
            ObjectAnimator.ofFloat(characterView, "alpha", 1f, 0f).apply {
                doOnEnd {
                    parent.mStoryContainer.removeView(characterView)
                }
            }
        }.also {
            AnimatorSet().playTogether(it.toList())
        }
    }

    private fun removeRedundantCharacterView(parent: View, indexesInPage: List<Int>) {
        parent.mStoryContainer.children.filterIndexed { index, view ->
            view.findViewById<FrameLayout>(R.id.mCharacterLayout) != null && index !in indexesInPage
        }.forEach {
            parent.mStoryContainer.removeView(it)
        }
    }

    private fun animationGenerateCharacterView(parent: View, wife: Wife, indexInPage: Int, index: Int) {
        val characterView: View = if (indexInPage < view!!.mStoryContainer.childCount - 2) {
            view!!.mStoryContainer.getChildAt(indexInPage)
        } else {
            generateNewCharacterEditView(parent, index).apply {
                parent.mStoryContainer.addView(this.apply {
                    alpha = 0f
                }, parent.mStoryContainer.childCount - 2)
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).start()
            }
        }
        ObjectAnimator.ofFloat(characterView, "alpha", 1f, 0f).apply {
            doOnEnd {
                characterView.mStoryCharacterImage.setImageResource(
                    viewModel.currentStoryPage.value!!.characterModels[index].let {
                        wife.res[it.clothesIndex].expressions[it.expressionIndex]
                    }
                )
                characterView.translationX = viewModel.currentStoryPage.value!!.characterModels[index].translateX
                characterView.translationY = viewModel.currentStoryPage.value!!.characterModels[index].translateY
                characterView.scaleX = viewModel.currentStoryPage.value!!.characterModels[index].scale
                characterView.scaleY = viewModel.currentStoryPage.value!!.characterModels[index].scale
                ObjectAnimator.ofFloat(characterView, "alpha", 0f, 1f).start()
            }
        }.start()

    }

    private fun generateCharacterView(parent: View, wife: Wife, indexInPage: Int, index: Int) {
        val characterView: View = if (indexInPage < view!!.mStoryContainer.childCount - 2) {
            view!!.mStoryContainer.getChildAt(indexInPage)
        } else {
            generateNewCharacterEditView(parent, index).apply {
                parent.mStoryContainer.addView(this.apply {
                    alpha = 0f
                }, parent.mStoryContainer.childCount - 2)
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).start()
            }
        }
        characterView.mStoryCharacterImage.setImageResource(
            viewModel.currentStoryPage.value!!.characterModels[index].let {
                wife.res[it.clothesIndex].expressions[it.expressionIndex]
            }
        )
        characterView.translationX = viewModel.currentStoryPage.value!!.characterModels[index].translateX
        characterView.translationY = viewModel.currentStoryPage.value!!.characterModels[index].translateY
        characterView.scaleX = viewModel.currentStoryPage.value!!.characterModels[index].scale
        characterView.scaleY = viewModel.currentStoryPage.value!!.characterModels[index].scale
    }

    private fun generateNewCharacterEditView(parent: View, index: Int): View {
        return layoutInflater.inflate(
            R.layout.widget_character,
            parent.mStoryContainer,
            false
        ).apply {
            val params = FrameLayout.LayoutParams(layoutParams.width, layoutParams.height)
            params.gravity = Gravity.CENTER
            layoutParams = params

            mCharacterLayout.translationX = viewModel.currentStoryPage.value!!.characterModels[index].translateX
            mCharacterLayout.translationY = viewModel.currentStoryPage.value!!.characterModels[index].translateY
            mCharacterLayout.scaleX = viewModel.currentStoryPage.value!!.characterModels[index].scale
            mCharacterLayout.scaleY = viewModel.currentStoryPage.value!!.characterModels[index].scale
        }
    }
}
