package com.yumetsuki.yuzusoftappwidget.page.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var isCreated = false

    private var isInitialize = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val wifeName = arguments!!.getString(CHARACTER_FRAGMENT_WIFE_NAME_EXTRA, Wife.Yoshino.wifeName)

        viewModel.wife.value = Wife.values().find {
            it.wifeName == wifeName
        }

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
        }

        return inflater.inflate(R.layout.character_viewpager_layout, container, false)
            .apply {

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
                    viewModel.chosenClothesIndex.value = viewModel.chosenClothesIndex.value!! - 1
                }

                mNextClothesBtn.setOnClickListener {
                    viewModel.chosenClothesIndex.value = viewModel.chosenClothesIndex.value!! + 1
                }

                initViewModelObserver()

            }
    }

    private fun View.initViewModelObserver () {

        viewModel.wife.observe(this@CharacterFragment, Observer { wife ->
            if (!isInitialize) {

                isInitialize = true

                viewModel.clothes.value = wife?.res.run {
                    this?.sortedBy {
                        if (it.clothesName ==
                            this@CharacterFragment.context!!
                                .applicationPref().getWifeClothesName(wife.wifeName)
                        ) {
                            0
                        } else {
                            1
                        }
                    }
                }

            }

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

        })

        viewModel.clothes.observe(this@CharacterFragment, Observer { wifeClothes ->
            viewModel.chosenClothesIndex.value = 0
            //显示角色立绘
            mCurrentViewWifeImage.setImageResource(wifeClothes[0].normalImage)
        })

        viewModel.chosenClothesIndex.observe(this@CharacterFragment, Observer { clothesIndex ->
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

            //改变角色立绘
            mCurrentViewWifeImage.setImageResource(viewModel.clothes.value!![clothesIndex].normalImage)
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isCreated && isVisibleToUser) {
            viewModel.wife.postValue(viewModel.wife.value)
        }
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