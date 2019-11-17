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
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.StoryPage
import com.yumetsuki.yuzusoftappwidget.page.character_select.CharacterSelectActivity
import com.yumetsuki.yuzusoftappwidget.page.story_edit.viewModel.StoryEditViewModel
import kotlinx.android.synthetic.main.fragment_story_edit.view.*
import kotlinx.android.synthetic.main.widget_character_editor.view.*

class StoryEditFragment: Fragment() {

    private val viewModel: StoryEditViewModel by lazy {
        ViewModelProvider(this).get(StoryEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.storyPages.value = arrayListOf(
            StoryPage(arrayListOf(), "")
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
            viewModel.storyPages.value!![viewModel.currentStoryPage.value!!].characters.forEachIndexed { index, character ->
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
            mAddCharacterBtn.setOnClickListener {
                startActivityForResult(Intent(
                    this@StoryEditFragment.activity, CharacterSelectActivity::class.java
                ), FOR_WIFE_SELECT)
                viewModel.storyPages
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
            ].characters[index].let {
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
                ].characters[index]
                viewModel.changeCharacterClothes(
                    index,
                    (character.clothesIndex + 1) % character.wife.res.size
                )
            }
            mChangeEditCharacterEmojiBtn.setOnClickListener {
                val character = viewModel.storyPages.value!![
                        viewModel.currentStoryPage.value!!
                ].characters[index]
                viewModel.changeCharacterExpression(
                    index,
                    (character.expressionIndex + 1) % character.wife.res[character.clothesIndex].expressions.size
                )
            }
        }
    }

    companion object {

        fun newInstance(): StoryEditFragment {
            return StoryEditFragment()
        }

        private const val FOR_WIFE_SELECT = 1000

    }

}