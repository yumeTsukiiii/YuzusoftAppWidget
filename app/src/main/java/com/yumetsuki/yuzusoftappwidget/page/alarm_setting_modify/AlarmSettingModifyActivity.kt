package com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Game
import kotlinx.android.synthetic.main.activity_alarm_setting_modify.*

class AlarmSettingModifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting_modify)
        setSupportActionBar(mAlarmSettingModifyToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mAlarmSettingModifyLayout.background = resources.getDrawable(
            getBackgroundImage(),
            resources.newTheme()
        )
    }

    companion object {
        const val ALARM_SETTINGS_BACKGROUND_EXTRA = "alarm_settings_background_extra"
        const val ALARM_SETTING_MODIFY_REQUEST_CODE = 100
    }

    private fun getBackgroundImage(): Int {
        return intent.getIntExtra(
            ALARM_SETTINGS_BACKGROUND_EXTRA,
            Game.Senrenbanka.backgroundImage
        )
    }

}
