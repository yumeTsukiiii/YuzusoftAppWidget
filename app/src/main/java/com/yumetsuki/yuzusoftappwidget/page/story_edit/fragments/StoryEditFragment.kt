package com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Background
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel
import com.yumetsuki.yuzusoftappwidget.page.background_select.BackgroundSelectActivity
import com.yumetsuki.yuzusoftappwidget.page.character_select.CharacterSelectActivity
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditFragViewModel
import kotlinx.android.synthetic.main.fragment_story_edit.view.*
import kotlinx.android.synthetic.main.widget_character_editor.view.*

class StoryEditFragment: Fragment() {

    private val viewModel: StoryEditFragViewModel by lazy {
        ViewModelProvider(this).get(StoryEditFragViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.storyPages.value = arrayListOf(
            StoryPageModel(arrayListOf(), null, "", Background.JinjiaBack.res)
        )
        viewModel.currentStoryPage.value = 0
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

        viewModel.storyPages.observe(this, Observer {
            view?.apply {
                mStoryEditBackgroundImg.setImageResource(
                    viewModel.storyPages.value!![viewModel.currentStoryPage.value!!].background
                )
            }
            viewModel.storyPages.value!![viewModel.currentStoryPage.value!!].characterModels.forEachIndexed { index, character ->
                generateCharacterEditView(view!!, character.wife, character.indexInPageView, index)
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.takeIf {
            requestCode == FOR_WIFE_SELECT
                    && resultCode == CharacterSelectActivity.FOR_WIFE_SELECT_RESULT
        }?.also {
            val wife = it.getSerializableExtra(CharacterSelectActivity.SELECT_WIFE) as Wife
            viewModel.addCharacterClothes(wife, view!!.mStoryEditContainer.childCount - 2)
        }

        data?.takeIf {
            requestCode == FOR_BACKGROUND_SELECT
                    && resultCode == BackgroundSelectActivity.FOR_BACKGROUND_SELECT_RESULT
        }?.also {
            val background = it.getSerializableExtra(BackgroundSelectActivity.SELECT_BACKGROUND) as Background
            viewModel.updateBackground(background)
        }
    }

    private fun generateCharacterEditView(parent: View, wife: Wife, indexInPage: Int, index: Int) {
        val characterView: View = if (indexInPage < view!!.mStoryEditContainer.childCount - 2) {
            view!!.mStoryEditContainer.getChildAt(indexInPage)
        } else {
            generateNewCharacterEditView(parent, index).apply {
                parent.mStoryEditContainer.addView(this, parent.mStoryEditContainer.childCount - 2)
            }
        }
        characterView.mEditCharacterImage.setImageResource(
            viewModel.storyPages.value!![
                    viewModel.currentStoryPage.value!!
            ].characterModels[index].let {
                wife.res[it.clothesIndex].expressions[it.expressionIndex]
            }
        )
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
                parent.mStoryEditContainer.removeView(this)
            }
            mChangeEditCharacterClothesBtn.setOnClickListener {
                val character = viewModel.storyPages.value!![
                        viewModel.currentStoryPage.value!!
                ].characterModels[index]
                viewModel.changeCharacterClothes(
                    index,
                    (character.clothesIndex + 1) % character.wife.res.size
                )
            }
            mChangeEditCharacterEmojiBtn.setOnClickListener {
                val character = viewModel.storyPages.value!![
                        viewModel.currentStoryPage.value!!
                ].characterModels[index]
                viewModel.changeCharacterExpression(
                    index,
                    (character.expressionIndex + 1) % character.wife.res[character.clothesIndex].expressions.size
                )
            }
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