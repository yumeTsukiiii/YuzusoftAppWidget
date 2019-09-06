package com.yumetsuki.yuzusoftappwidget.page.alarm_settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yumetsuki.yuzusoftappwidget.config.Game
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.utils.checkAlarmServiceRunning
import com.yumetsuki.yuzusoftappwidget.utils.toast

class AlarmSettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_settings)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,
                AlarmSettingsFragment.newInstance(
                    getBackgroundImage()
                )
            ).commit()
    }

    override fun onResume() {
        super.onResume()
        if (!checkAlarmServiceRunning()) {
            toast("闹钟服务已停止运行")
            finish()
        }
    }

    private fun getBackgroundImage(): Int {
        return intent.getIntExtra(
            ALARM_SETTINGS_BACKGROUND_EXTRA,
            Game.Senrenbanka.backgroundImage
        )
    }

    companion object {
        const val ALARM_SETTINGS_BACKGROUND_EXTRA = "alarm_settings_background_extra"
    }
}