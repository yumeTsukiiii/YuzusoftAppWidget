package com.yumetsuki.yuzusoftappwidget.page.app_info.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.app_info.viewmodel.AppInfoViewModel
import kotlinx.android.synthetic.main.fragment_app_info.view.*

class AppInfoFragment : Fragment() {

    private val viewModel: AppInfoViewModel by lazy {
        ViewModelProvider(this).get(AppInfoViewModel::class.java)
    }

    private var backgroundImageResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundImageResource = arguments!!.getInt(ALARM_SETTINGS_BACKGROUND_ARGUMENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_app_info, container, false).apply {
            mAppInfoLayout.background =
                resources.getDrawable(backgroundImageResource!!, resources.newTheme())

            (activity!! as AppCompatActivity).apply {
                setSupportActionBar(mAboutAppToolbar)
                mAboutAppToolbar.setNavigationOnClickListener {
                    finish()
                }
            }

            initTextFont()
        }
    }

    private fun View.initTextFont() {
        mAboutAppTitle.typeface = Typeface.createFromAsset(context.assets, "fonts/简罗卜.ttf")
        mAboutAuthorTitle.typeface = Typeface.createFromAsset(context.assets, "fonts/简罗卜.ttf")
        mOnegaiTitle.typeface = Typeface.createFromAsset(context.assets, "fonts/简罗卜.ttf")
    }

    companion object {

        private const val ALARM_SETTINGS_BACKGROUND_ARGUMENT = "alarm_settings_background_argument"

        fun newInstance(backgroundImageResource: Int): AppInfoFragment {
            return AppInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALARM_SETTINGS_BACKGROUND_ARGUMENT, backgroundImageResource)
                }
            }
        }
    }
}
