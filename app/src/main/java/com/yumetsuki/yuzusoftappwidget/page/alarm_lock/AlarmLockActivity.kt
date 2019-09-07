package com.yumetsuki.yuzusoftappwidget.page.alarm_lock

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.KeyguardManager
import android.app.Service
import android.content.Context
import android.os.*
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import com.yumetsuki.yuzusoftappwidget.*
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.service.TimeReminder
import com.yumetsuki.yuzusoftappwidget.utils.applicationPref
import com.yumetsuki.yuzusoftappwidget.utils.formatTimeText
import kotlinx.android.synthetic.main.activity_alarm_lock.*
import kotlinx.coroutines.*

class AlarmLockActivity : AppCompatActivity() {

    private lateinit var alarmJob: Job

    private val vibrator by lazy {
        getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_lock)
        //锁屏时显示
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        )

        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        }

        val alarmSettingModel = intent.getParcelableExtra<AlarmSettingModel>(
            ALARM_SETTING_MODEL_EXTRA
        )

        val wife = Wife.values().find { it.wifeName == CharacterConfig.mostLikeWifeName }!!

        mLockTimeText.text = alarmSettingModel.formatTimeText()

        mLockActivityLayout.background = resources.getDrawable(
            wife.gameBelong.backgroundImage,
            resources.newTheme()
        )

        mCharacterImage.setImageResource(
            wife.res.find {
                it.clothesName == applicationPref().getWifeClothesName(wife.wifeName)
            }!!.normalImage
        )

        mLockControlBtn.setImageResource(
            alarmSettingModel.alarmSetting.icon
        )

        mLockControlBtn.setOnLongClickListener {
            lock()
            true
        }

        playTipAnimation()

        alarmJob = playTimeVoice()

    }

    override fun onDestroy() {
        super.onDestroy()
        if(this::alarmJob.isInitialized) {
            if (!alarmJob.isCancelled) {
                alarmJob.cancel()
            }
        }
    }

    /**
     * 方式锁屏后退
     * */
    override fun onBackPressed() {

    }

    /**
     * 播放解锁提示文字的动画
     * */
    private fun playTipAnimation() {

        AnimatorSet().apply {

            play(
                ObjectAnimator.ofFloat(mLockTipText, "translationY", 0f, 50f)
            ).with(
                ObjectAnimator.ofFloat(mLockTipText, "alpha", 0f, 1f)
            )

        }.start()

        ObjectAnimator.ofFloat(mLockTipText, "rotation", -10f, 10f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = 60
        }.start()
    }

    /**
     * 解锁activity
     * */
    private fun lock() {
        AnimatorSet().apply {
            doOnEnd {
                finish()
            }
            play(
                ObjectAnimator.ofFloat(mLockControlBtn, "translationY", 0f, -400f)
            ).with(
                ObjectAnimator.ofFloat(mLockControlBtn, "alpha", 1f, 0f)
            ).after(
                ObjectAnimator.ofFloat(mLockControlBtn, "rotation", 0f, 1080f)
            )
        }.start()
    }

    /**
     * 语音报时，报时10次后自动停止
     * */
    private fun playTimeVoice(): Job {
        return GlobalScope.launch {
            val hasVibrator = vibrator.hasVibrator()
            repeat(10) {
                //TODO("待优化")
                if (hasVibrator) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1500, DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(1500)
                    }
                }
                TimeReminder.playVoiceAtTime(this@AlarmLockActivity)
                delay(1500)
            }
            vibrator.cancel()
            finish()
        }
    }

    companion object {
        const val ALARM_SETTING_MODEL_EXTRA = "alarm_setting_model_extra"
    }
}
