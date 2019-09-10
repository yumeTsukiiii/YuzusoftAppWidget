package com.yumetsuki.yuzusoftappwidget.page.main.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.CharacterConfig
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.getWifeClothesName
import com.yumetsuki.yuzusoftappwidget.page.main.viewmodel.CharacterFragmentViewModel
import com.yumetsuki.yuzusoftappwidget.utils.applicationPref
import kotlinx.android.synthetic.main.character_viewpager_layout.view.*

class CharacterFragment: Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.activity!!.application))
            .get(CharacterFragmentViewModel::class.java)
    }

    private var wifeMusicAnimationSet: AnimatorSet? = null

    private var isCreated = false

    private var isInitialize = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val wifeName = arguments!!.getString(CHARACTER_FRAGMENT_WIFE_NAME_EXTRA, Wife.Yoshino.wifeName)

        viewModel.wife.value = Wife.values().find {
            it.wifeName == wifeName
        }

        viewModel.isMusicPlaying.value = false

        isCreated = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.apply {
            wife.removeObservers(this@CharacterFragment)
            clothes.removeObservers(this@CharacterFragment)
            chosenClothesIndex.removeObservers(this@CharacterFragment)
            isMusicPlaying.removeObservers(this@CharacterFragment)
        }

        return inflater.inflate(R.layout.character_viewpager_layout, container, false)
            .apply {

                initTextFont()

                mConfirmClothesBtn.setOnClickListener {
                    viewModel.updateWifeClothes(this@CharacterFragment.context!!)
                    mConfirmClothesBtn.isEnabled = false
                    mConfirmClothesBtn.text = if (mConfirmClothesBtn.isEnabled) {
                        "选中服装"
                    } else {
                        "已选中服装"
                    }
                }

                mConfirmCharacterBtn.setOnClickListener {
                    viewModel.updateMostLikeWife(this@CharacterFragment.context!!)

                    mConfirmCharacterBtn.isEnabled = false

                    mConfirmCharacterBtn.text = if (mConfirmCharacterBtn.isEnabled) {
                        "选中角色"
                    } else {
                        "已选中角色"
                    }
                }

                mPreviousClothesBtn.setOnClickListener {
                    viewModel.previousClothesIndex.value = viewModel.chosenClothesIndex.value
                    viewModel.chosenClothesIndex.value = viewModel.chosenClothesIndex.value!! - 1
                }

                mNextClothesBtn.setOnClickListener {
                    viewModel.previousClothesIndex.value = viewModel.chosenClothesIndex.value
                    viewModel.chosenClothesIndex.value = viewModel.chosenClothesIndex.value!! + 1
                }

                mWifeAvatar.setOnLongClickListener {
                    viewModel.isMusicPlaying.value = !(viewModel.isMusicPlaying.value?:false)
                    true
                }

                initViewModelObserver()

            }
    }

    /**
     * 设置字体样式
     * */
    private fun View.initTextFont() {
        mChosenClothesText.typeface = Typeface.createFromAsset(context.assets, "fonts/简哈哈.ttf")
        mConfirmClothesBtn.typeface = Typeface.createFromAsset(context.assets, "fonts/简哈哈.ttf")
        mConfirmCharacterBtn.typeface = Typeface.createFromAsset(context.assets, "fonts/简哈哈.ttf")
        mWifeName.typeface = Typeface.createFromAsset(context.assets, "fonts/简罗卜.ttf")
    }

    private fun View.initViewModelObserver () {

        viewModel.wife.observe(this@CharacterFragment, Observer { wife ->
            if (!isInitialize) {

                isInitialize = true

                changeWifeClothes(wife)

            }

            changeWifeClothesUI(wife)

        })

        viewModel.clothes.observe(this@CharacterFragment, Observer { wifeClothes ->
            viewModel.chosenClothesIndex.value = 0
            viewModel.previousClothesIndex.value = 0
            //显示角色立绘
            mCurrentViewWifeImage.setImageResource(wifeClothes[0].normalImage)
        })

        viewModel.chosenClothesIndex.observe(this@CharacterFragment, Observer { clothesIndex ->

            changeClothesControlUIStatus(clothesIndex)

            //改变角色立绘
            if (viewModel.previousClothesIndex.value == viewModel.chosenClothesIndex.value) {
                mCurrentViewWifeImage.setImageResource(viewModel.clothes.value!![clothesIndex].normalImage)
            } else {
                ObjectAnimator.ofFloat(mCurrentViewWifeImage, "alpha", 1f, 0f).apply {
                    doOnEnd {
                        mCurrentViewWifeImage.setImageResource(viewModel.clothes.value!![clothesIndex].normalImage)
                        ObjectAnimator.ofFloat(mCurrentViewWifeImage, "alpha", 0f, 1f).start()
                    }
                }.start()
            }
        })

        viewModel.isMusicPlaying.observe(this@CharacterFragment, Observer { isPlaying ->
            if (isPlaying && mWifeMusicIcon.visibility == View.GONE) {
                startPlayWifeMusic()
                showMusicIcon()
            } else if (!isPlaying && mWifeMusicIcon.visibility == View.VISIBLE) {
                stopPlayWifeMusic()
                hideMusicIcon()
            }
        })


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isCreated && isVisibleToUser) {
            viewModel.wife.postValue(viewModel.wife.value)
        }
        if (isCreated && !isVisibleToUser) {
            viewModel.isMusicPlaying.value = false
        }
    }

    private fun changeWifeClothes(wife: Wife?) {
        viewModel.clothes.value = wife?.res.run {
            this?.sortedBy {
                if (it.clothesName ==
                    this@CharacterFragment.context!!
                        .applicationPref().getWifeClothesName(wife!!.wifeName)
                ) {
                    0
                } else {
                    1
                }
            }
        }
    }

    private fun View.changeWifeClothesUI(wife: Wife) {
        mWifeAvatar.setImageResource(wife.avatar)
        mWifeName.text = wife.chineseTransName

        //该角色已经被选中了
        mConfirmCharacterBtn.isEnabled =
            viewModel.wife.value!!.wifeName != CharacterConfig.mostLikeWifeName
        mConfirmCharacterBtn.text = if (mConfirmCharacterBtn.isEnabled) {
            "选中角色"
        } else {
            "已选中角色"
        }

    }

    private fun View.changeClothesControlUIStatus(clothesIndex: Int) {
        //控制上下选择服装
        mPreviousClothesBtn.isEnabled = clothesIndex != 0

        mNextClothesBtn.isEnabled = clothesIndex != viewModel.clothes.value!!.lastIndex
        mChosenClothesText.text = viewModel.clothes.value!![clothesIndex].clothesName

        //该服装是否已经被选中
        mConfirmClothesBtn.isEnabled = viewModel.clothes.value!![clothesIndex].clothesName != this@CharacterFragment.context!!
            .applicationPref().getWifeClothesName(viewModel.wife.value!!.wifeName)
        mConfirmClothesBtn.text = if (mConfirmClothesBtn.isEnabled) {
            "选中服装"
        } else {
            "已选中服装"
        }
    }

    private fun startPlayWifeMusic() {
        viewModel.startPlayCharacterMusic(context)
    }

    private fun stopPlayWifeMusic() {
        viewModel.stopPlayCharacterMusic()
    }

    private fun View.showMusicIcon() {

        AnimatorSet().apply {

            doOnStart {
                mWifeMusicIcon.visibility = View.VISIBLE
                mWifeMusicAnimationIcon.visibility = View.VISIBLE
            }

            doOnEnd {
                wifeMusicAnimationSet = AnimatorSet().apply {

                    duration = 1000

                    play(
                        ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "scaleX", 1f, 3f).apply {
                            repeatCount = ValueAnimator.INFINITE
                            repeatMode = ValueAnimator.RESTART
                        }
                    ).with(
                        ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "scaleY", 1f, 3f).apply {
                            repeatCount = ValueAnimator.INFINITE
                            repeatMode = ValueAnimator.RESTART
                        }
                    ).with(
                        ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "alpha", 1f, 0f).apply {
                            repeatCount = ValueAnimator.INFINITE
                            repeatMode = ValueAnimator.RESTART
                        }
                    )

                    start()
                }
            }

            play(
                ObjectAnimator.ofFloat(mWifeMusicIcon, "alpha", 0f, 1f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicIcon, "translationX", 0f, 50f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "alpha", 0f, 1f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "translationX", 0f, 50f)
            )

        }.start()

    }

    private fun View.hideMusicIcon() {
        wifeMusicAnimationSet?.cancel()

        AnimatorSet().apply {

            doOnEnd {
                mWifeMusicIcon.visibility = View.GONE
                mWifeMusicAnimationIcon.visibility = View.GONE
            }

            play(
                ObjectAnimator.ofFloat(mWifeMusicIcon, "alpha", 1f, 0f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicIcon, "translationX", 50f, 0f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "alpha", 1f, 0f)
            ).with(
                ObjectAnimator.ofFloat(mWifeMusicAnimationIcon, "translationX", 50f, 0f)
            )

        }.start()
    }

    companion object {

        const val CHARACTER_FRAGMENT_WIFE_NAME_EXTRA = "character_fragment_wife_name_extra"

        fun newInstance(wifeName: String): CharacterFragment {
            return CharacterFragment().apply {
                arguments = Bundle().also {
                    it.putString(CHARACTER_FRAGMENT_WIFE_NAME_EXTRA, wifeName)
                }
            }
        }
    }
}