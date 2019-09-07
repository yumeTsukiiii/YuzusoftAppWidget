package com.yumetsuki.yuzusoftappwidget.page.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.*
import com.yumetsuki.yuzusoftappwidget.config.Game
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.AlarmSettingsActivity
import com.yumetsuki.yuzusoftappwidget.page.main.adapter.CharacterViewPagerAdapter
import com.yumetsuki.yuzusoftappwidget.page.main.fragments.CharacterFragment
import com.yumetsuki.yuzusoftappwidget.page.main.viewmodel.MainViewModel
import com.yumetsuki.yuzusoftappwidget.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var backTime: Long? = null

    private var isMenuStatusChanging = false

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mMainToolbar)

        initClothesStatus()
        initAppWidgetState()

        initGameChangeBtn()

        initMenusBtn()

        viewModel.currentGameIndex.value = 0

        viewModel.currentGameIndex.observe(this, androidx.lifecycle.Observer {

            mPreviousGameBtn.isEnabled = it != 0

            mNextGameBtn.isEnabled = it != Game.values().size - 1

            Game.values()[it].apply {
                initBackgroundAndToolbarInnerImage()
                initCharacterViewPager()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        checkNotificationPermission()
    }

    private fun initAppWidgetState() {
        Status.isCharacterPlaying = false
    }

    private fun Game.initBackgroundAndToolbarInnerImage() {
        mToolbarInnerImage.setImageResource(tabImage)
        mMainLayout.setBackgroundResource(backgroundImage)
    }

    /**
     * 切换当前选择老婆所属的游戏
     * */
    private fun initGameChangeBtn() {
        mPreviousGameBtn.setOnClickListener {
            viewModel.currentGameIndex.value = viewModel.currentGameIndex.value!! - 1
        }

        mNextGameBtn.setOnClickListener {
            viewModel.currentGameIndex.value = viewModel.currentGameIndex.value!! + 1
        }
    }

    /**
     * 初始化menu btn点击事件，触发动画
     * */
    @SuppressLint("RestrictedApi")
    private fun initMenusBtn() {

        mAlarmNavigatorActionBtn.setOnClickListener {
            navigateToAlarmSetting()
        }

        mMenuActionExpendBtn.setOnClickListener {

            if (isMenuStatusChanging) return@setOnClickListener

            isMenuStatusChanging = true

            if (mAboutNavigatorActionBtn.visibility == View.GONE) {
                showSubMenuBtn()
            } else {
                hideSubMenuBtn()
            }

        }
    }

    /**
     * 显示子菜单
     * */
    @SuppressLint("RestrictedApi")
    private fun showSubMenuBtn() {
        mAboutNavigatorActionBtn.visibility = View.VISIBLE
        mAlarmNavigatorActionBtn.visibility = View.VISIBLE
        AnimatorSet().apply {

            doOnEnd { isMenuStatusChanging = false }

            play(
                ObjectAnimator.ofFloat(mAboutNavigatorActionBtn, "alpha", 0f, 1f)
            ).with(
                ObjectAnimator.ofFloat(mAboutNavigatorActionBtn, "translationX",  0f, -200f)
            ).with(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "alpha", 0f, 1f)
            ).with(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationX",  0f, -200f)
            ).before(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationX",  -200f, 0f).apply {
                    interpolator = AccelerateInterpolator()
                }
            ).before(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationY",  0f, -200f).apply {
                    interpolator = DecelerateInterpolator()
                }
            )
        }.start()
    }

    /**
     * 隐藏子菜单
     * */
    @SuppressLint("RestrictedApi")
    private fun hideSubMenuBtn() {
        AnimatorSet().apply {
            doOnEnd {
                mAboutNavigatorActionBtn.visibility = View.GONE
                mAlarmNavigatorActionBtn.visibility = View.GONE
                isMenuStatusChanging = false
            }
            play(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationX",  0f, -200f).apply {
                    interpolator = DecelerateInterpolator()
                }
            ).with(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationY",  -200f, 0f).apply {
                    interpolator = AccelerateInterpolator()
                }
            ).before(
                ObjectAnimator.ofFloat(mAboutNavigatorActionBtn, "alpha", 1f, 0f)
            ).before(
                ObjectAnimator.ofFloat(mAboutNavigatorActionBtn, "translationX",  -200f, 0f)
            ).before(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "alpha", 1f, 0f)
            ).before(
                ObjectAnimator.ofFloat(mAlarmNavigatorActionBtn, "translationX",  -200f, 0f)
            )

        }.start()
    }

    private fun navigateToAlarmSetting() {

        if (!checkAlarmServiceRunning()) {
            alert {
                setTitle("提示")
                setMessage("请先开启闹钟功能哦～")
                setPositiveButton("确认", null)
            }
            return
        }

        startActivity(
            Intent(
                this,
                AlarmSettingsActivity::class.java
            ).apply {
                putExtra(
                    AlarmSettingsActivity.ALARM_SETTINGS_BACKGROUND_EXTRA,
                    Game.values()[viewModel.currentGameIndex.value?:0].backgroundImage
                )
            }
        )
    }

    /**
     * 设置ViewPager显示的老婆！
     * */
    private fun Game.initCharacterViewPager() {
        mCharacterViewPager.offscreenPageLimit = 0
        mCharacterViewPager.adapter = CharacterViewPagerAdapter(
            supportFragmentManager,
            Wife.values().filter {
                it.gameBelong.gameName == gameName
            }.run {
                sortedBy {
                    if (it.wifeName == CharacterConfig.mostLikeWifeName) {
                        0
                    } else {
                        1
                    }
                }
            }.map {
                CharacterFragment.newInstance(it.wifeName)
            }
        )
    }

    /**
     * 检查是否有通知权限
     * */
    private fun checkNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            AlertDialog.Builder(this)
                .setTitle("重要提示！")
                .setMessage("您的设备为对该应用开放通知权限，这将导致报时和闹钟功能可能不可用哦～(部分手机跳转不到正确的开启界面请手动设置～)")
                .setNegativeButton("我不管！") { dialog, which ->
                    dialog.cancel()
                }.setPositiveButton("赶紧开启") { dialog, which ->
                    dialog.cancel()
                    openNotificationSetting()
                }.show()
        }
    }

    /**
     * 打开通知权限设置
     * */
    private fun openNotificationSetting() {
        Intent(
            if (Build.VERSION.SDK_INT >= 26) {
                Settings.ACTION_APP_NOTIFICATION_SETTINGS
            } else {
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            }
        ).apply {
            when {
                Build.VERSION.SDK_INT >= 26 -> {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)
                }
                //api 21以上的权限设置打开方案
                Build.VERSION.SDK_INT >= 23 -> {
                    putExtra("app_package", packageName)
                    putExtra("app_uid", applicationInfo.uid)
                }
                else -> putExtra("package", packageName)
            }

            try {
                startActivity(this)
            } catch (e: Exception) {
                //最低级默认方案
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                    startActivity(this)
                }
            }
        }
    }

    /**
     * 初始化服装选择
     * */
    private fun initClothesStatus() {
        Wife.values().forEach {
            applicationPref().apply {
                getWifeClothesName(it.wifeName)
                    .also { clothes ->
                        if (clothes.isEmpty()) {
                            putWifeClothes(it.wifeName, it.res[0].clothesName)
                        }
                    }
            }
        }
    }

    override fun onBackPressed() {
        if (Calendar.getInstance().timeInMillis - (backTime?:0) > 2000) {
            Toast.makeText(this, "你确定要离开老婆们吗？！", Toast.LENGTH_SHORT).show()
            backTime = Calendar.getInstance().timeInMillis
        } else {
            finish()
            exitProcess(0)
        }
    }

}
