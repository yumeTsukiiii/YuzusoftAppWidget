package com.yumetsuki.yuzusoftappwidget.page.app_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Game
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.AlarmSettingsActivity
import com.yumetsuki.yuzusoftappwidget.page.app_info.fragments.AppInfoFragment

class AppInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_info_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AppInfoFragment.newInstance(
                    getBackgroundImage()
                )).commitNow()
        }
    }

    private fun getBackgroundImage(): Int {
        return intent.getIntExtra(
            AlarmSettingsActivity.ALARM_SETTINGS_BACKGROUND_EXTRA,
            Game.Senrenbanka.backgroundImage
        )
    }

    companion object {
        const val ALARM_SETTINGS_BACKGROUND_EXTRA = "alarm_settings_background_extra"
    }

}
