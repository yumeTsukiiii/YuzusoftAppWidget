package com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Background
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.page.background_select.BackgroundSelectActivity
import com.yumetsuki.yuzusoftappwidget.page.character_select.CharacterSelectActivity
import com.yumetsuki.yuzusoftappwidget.page.story_edit.adapter.HistoryItemAdapter
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditActViewModel
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditFragViewModel
import com.yumetsuki.yuzusoftappwidget.utils.toast
import com.yumetsuki.yuzusoftappwidget.widget.DragZoomLayout
import kotlinx.android.synthetic.main.fragment_story_edit.view.*
import kotlinx.android.synthetic.main.widget_character_editor.view.*

class StoryEditFragment: Fragment() {

    private val viewModel: StoryEditFragViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryEditFragViewModel::class.java)
    }

    private val actViewModel: StoryEditActViewModel by lazy {
        ViewModelProvider(this.activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application)).get(StoryEditActViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actViewModel.previousEditRecord.value?.chapterId.takeIf {
            it == actViewModel.currentChapterId.value
        }?.let {
            viewModel.requestStoryPageByRecordPageId(actViewModel.currentChapterId.value!!, actViewModel.previousEditRecord.value!!.pageId)
        }?:viewModel.requestLastStoryPageModelByChapterId(actViewModel.currentChapterId.value!!)

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

        viewModel.currentStoryPage.observe(this, Observer {
            view?.apply {
                mStoryEditBackgroundImg.setImageResource(
                    it.background
                )
                if (mSpeakerNameEditText.text.toString() != it.header) {
                    mSpeakerNameEditText.setText(it.header?:"")
                }
                if (mSpeakerContentEditText.text.toString() != it.content) {
                    mSpeakerContentEditText.setText(it.content)
                }
            }
            removeRedundantCharacterEditView(view!!, it.characterModels.map { model -> model.indexInPageView })
            it.characterModels.forEachIndexed { index, character ->
                generateCharacterEditView(view!!, character.wife, character.indexInPageView, index)
            }
        })

        viewModel.isShowHistory.observe(this, Observer {
            view?.apply {
                mHistoryLayout.visibility = if (it) {
                    viewModel.requestAllHistories()
                    View.VISIBLE
                } else {
                    viewModel.histories.value = listOf()
                    View.GONE
                }
            }
        })

        viewModel.toastTip.observe(this, Observer {
            context?.toast(it)
        })

        viewModel.histories.observe(this, Observer {
            view?.apply {
                (mHistoryRecyclerView.adapter as? HistoryItemAdapter)?.notifyDataSetChanged()
                viewModel.histories.value!!.indexOfFirst {
                    it.pageId == viewModel.currentStoryPage.value!!.id
                }.takeIf {
                    it != -1
                }?.also {  indexOfPage ->
                    mHistoryRecyclerView.scrollToPosition(
                        indexOfPage
                    )
                }
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            mToggleRightToolDrawerImg.setOnClickListener {
                viewModel.isDrawerExpand.value = !(viewModel.isDrawerExpand.value?:false)
            }
            mSwitchBackgroundBtn.setOnClickListener {
                startActivityForResult(Intent(
                    this@StoryEditFragment.activity, BackgroundSelectActivity::class.java
                ), FOR_BACKGROUND_SELECT)
            }
            mAddCharacterBtn.setOnClickListener {
                startActivityForResult(Intent(
                    this@StoryEditFragment.activity, CharacterSelectActivity::class.java
                ), FOR_WIFE_SELECT)
            }
            mSaveCurrentPageBtn.setOnClickListener {
                viewModel.saveCurrentPage().invokeOnCompletion {
                    actViewModel.updatePreviousEditRecord(
                        viewModel.currentStoryPage.value!!.chapterId,
                        viewModel.currentStoryPage.value!!.id
                    )
                }
            }
            mExitEditBtn.setOnClickListener {
                this@StoryEditFragment.activity?.finish()
            }
            mNextPageBtn.setOnClickListener {
                viewModel.nextPage().invokeOnCompletion {
                    actViewModel.updatePreviousEditRecord(
                        viewModel.currentStoryPage.value!!.chapterId,
                        viewModel.currentStoryPage.value!!.id
                    )
                }
            }
            mHistoryEditPageBtn.setOnClickListener {
                viewModel.isShowHistory.value = true
            }
            mCloseHistoryPageBtn.setOnClickListener {
                viewModel.isShowHistory.value = false
            }
            mHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
            mHistoryRecyclerView.adapter = HistoryItemAdapter(
                viewModel.histories,
                { simpleHistory, _ ->
                    viewModel.jumpCurrentPage(simpleHistory.pageId)
                    viewModel.isShowHistory.value = false
                }
            ) {
                simpleHistory, _ ->
                viewModel.removePage(simpleHistory.pageId)
                viewModel.isShowHistory.value = false
            }
            mSpeakerNameEditText.addTextChangedListener(object :TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updatePageModelHeader(s?.toString())
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
            mSpeakerContentEditText.addTextChangedListener(object :TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.updatePageModelContent(s?.toString()?:"")
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.takeIf {
            requestCode == FOR_WIFE_SELECT
                    && resultCode == CharacterSelectActivity.FOR_WIFE_SELECT_RESULT
        }?.also {
            val wife = it.getSerializableExtra(CharacterSelectActivity.SELECT_WIFE) as Wife
            viewModel.addCharacterClothes(wife, view!!.mStoryEditContainer.childCount - 3)
        }

        data?.takeIf {
            requestCode == FOR_BACKGROUND_SELECT
                    && resultCode == BackgroundSelectActivity.FOR_BACKGROUND_SELECT_RESULT
        }?.also {
            val background = it.getSerializableExtra(BackgroundSelectActivity.SELECT_BACKGROUND) as Background
            viewModel.updateBackground(background)
        }
    }

    private fun removeRedundantCharacterEditView(parent: View, indexesInPage: List<Int>) {
        parent.mStoryEditContainer.children.filterIndexed { index, view ->
            view is DragZoomLayout && index !in indexesInPage
        }.forEach {
            parent.mStoryEditContainer.removeView(it)
        }
    }

    private fun generateCharacterEditView(parent: View, wife: Wife, indexInPage: Int, index: Int) {
        val characterView: View = if (indexInPage < view!!.mStoryEditContainer.childCount - 3) {
            view!!.mStoryEditContainer.getChildAt(indexInPage)
        } else {
            generateNewCharacterEditView(parent, index).apply {
                parent.mStoryEditContainer.addView(this, parent.mStoryEditContainer.childCount - 3)
            }
        }
        characterView.mEditCharacterImage.setImageResource(
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
            R.layout.widget_character_editor,
            parent.mStoryEditContainer,
            false
        ).apply {
            val params = FrameLayout.LayoutParams(layoutParams.width, layoutParams.height)
            params.gravity = Gravity.CENTER
            layoutParams = params
            mDeleteEditCharacterBtn.setOnClickListener {
                viewModel.removeCharacterClothes(viewModel.currentStoryPage.value!!.characterModels[index])
                parent.mStoryEditContainer.removeView(this)
            }
            mChangeEditCharacterClothesBtn.setOnClickListener {
                val character = viewModel.currentStoryPage.value!!.characterModels[index]
                viewModel.changeCharacterClothes(
                    index,
                    (character.clothesIndex + 1) % character.wife.res.size
                )
            }
            mChangeEditCharacterEmojiBtn.setOnClickListener {
                val character = viewModel.currentStoryPage.value!!.characterModels[index]
                viewModel.changeCharacterExpression(
                    index,
                    (character.expressionIndex + 1) % character.wife.res[character.clothesIndex].expressions.size
                )
            }
            mDragCharacter.translationX = viewModel.currentStoryPage.value!!.characterModels[index].translateX
            mDragCharacter.translationY = viewModel.currentStoryPage.value!!.characterModels[index].translateY
            mDragCharacter.scaleX = viewModel.currentStoryPage.value!!.characterModels[index].scale
            mDragCharacter.scaleY = viewModel.currentStoryPage.value!!.characterModels[index].scale
            mDragCharacter.setOnScaleListener { scaleX, _ ->
                viewModel.changeCharacterScale(index, scaleX)
            }
            mDragCharacter.setOnTranslateListener { translateX, translateY ->
                viewModel.changeCharacterTranslate(index, translateX, translateY)
            }
        }
    }

    companion object {

        fun newInstance(): StoryEditFragment {
            return StoryEditFragment()
        }

        private const val FOR_WIFE_SELECT = 1000

        private const val FOR_BACKGROUND_SELECT = 200

    }

}