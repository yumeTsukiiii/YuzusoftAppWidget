package com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Game
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.model.DateChosen
import com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify.adapter.DateRemindChooseAdapter
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays
import com.yumetsuki.yuzusoftappwidget.utils.checkAlarmServiceRunning
import com.yumetsuki.yuzusoftappwidget.utils.formatDaysText
import com.yumetsuki.yuzusoftappwidget.utils.toast
import kotlinx.android.synthetic.main.activity_alarm_setting_modify.*
import kotlinx.android.synthetic.main.date_remind_choose_bottom_sheet.view.*

class AlarmSettingModifyActivity : AppCompatActivity() {

    private val viewModel: AlarmSettingModifyViewModel by lazy {
        ViewModelProvider(this).get(AlarmSettingModifyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting_modify)
        setSupportActionBar(mAlarmSettingModifyToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val alarmSettingModel = getAlarmSettingModel().apply {
            viewModel.alarmSettingModel = this
        }

        initViewModel(alarmSettingModel)

        mAlarmSettingModifyLayout.background = resources.getDrawable(
            getBackgroundImage(),
            resources.newTheme()
        )

        mAlarmDayLayout.setOnClickListener {
            buildBottomSheet()
        }

        mAlarmSettingModifyTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            viewModel.alarmHour.value = hourOfDay
            viewModel.alarmMinutes.value = minute
        }

        mAlarmSettingModifyTimePicker.hour = alarmSettingModel.alarmSetting.alarmHour
        mAlarmSettingModifyTimePicker.minute = alarmSettingModel.alarmSetting.alarmMinute
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_alarm_mofity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.modify_done_item -> {
                setResult(ALARM_SETTING_MODIFY_RESULT_CODE, Intent().apply {
                    putExtra(ALARM_SETTING_ITEM_EXTRA, getCompleteAlarm())
                })
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (!checkAlarmServiceRunning()) {
            toast("闹钟服务已停止运行")
            finish()
        }
    }

    private fun initViewModel(alarmSettingModel: AlarmSettingModel) {

        viewModel.alarmHour.value = alarmSettingModel.alarmSetting.alarmHour
        viewModel.alarmMinutes.value = alarmSettingModel.alarmSetting.alarmMinute
        viewModel.alarmIcon.value = alarmSettingModel.alarmSetting.icon
        viewModel.dateChosen.value = (1..7).map {
            DateChosen(
                it,
                getDateText(it),
                alarmSettingModel.days.any { day -> day.day == it }
            )
        } as ArrayList<DateChosen>

        viewModel.dateChosen.observe(this, Observer {
            viewModel.dateRemindChooseAdapter?.notifyDataSetChanged()
            mAlarmDayText.text = formatDaysText(it.filter { chosen -> chosen.isEnable }.map { chosen -> chosen.value })
        })

    }

    private fun buildBottomSheet() {

        BottomSheetDialog(this).apply {
            setContentView(
                initBottomSheetView(
                    View.inflate(
                        this@AlarmSettingModifyActivity,
                        R.layout.date_remind_choose_bottom_sheet,
                        null
                    )
                )
            )
            window?.apply {
                findViewById<View>(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent)
            }
        }.show()

    }

    private fun initBottomSheetView(view: View): View {
        return view.apply {
            mDateRemindChooseRecyclerView.adapter = DateRemindChooseAdapter(
                viewModel.dateChosen,
                onDateChosen = { position, isEnable ->
                    viewModel.changeDateChosenStatus(position, isEnable)
                }
            ).apply {
                viewModel.dateRemindChooseAdapter = this
            }
            mDateRemindChooseRecyclerView.layoutManager = LinearLayoutManager(this@AlarmSettingModifyActivity)
        }
    }

    private fun getBackgroundImage(): Int {
        return intent.getIntExtra(
            ALARM_SETTINGS_BACKGROUND_EXTRA,
            Game.Senrenbanka.backgroundImage
        )
    }

    private fun getAlarmSettingModel(): AlarmSettingModel {
        return intent.getParcelableExtra(
            ALARM_SETTING_ITEM_EXTRA
        )
    }

    private fun getDateText(dateId: Int): String {
        return when(dateId) {
            1 -> "周一"
            2 -> "周二"
            3 -> "周三"
            4 -> "周四"
            5 -> "周五"
            6 -> "周六"
            7 -> "周日"
            else -> error("没有这一天的日子啊")
        }
    }

    private fun getCompleteAlarm(): AlarmSettingModel {
        return AlarmSettingModel(
            AlarmSetting(
                viewModel.alarmSettingModel!!.alarmSetting.id,
                viewModel.alarmIcon.value!!,
                viewModel.alarmHour.value!!,
                viewModel.alarmMinutes.value!!,
                true
            ),
            viewModel.dateChosen.value!!.filter { it.isEnable }
                .map {
                    AlarmSettingDays(
                        -1,
                        it.value,
                        viewModel.alarmSettingModel!!.alarmSetting.id
                    )
                }
        )
    }

    companion object {
        const val ALARM_SETTINGS_BACKGROUND_EXTRA = "alarm_settings_background_extra"
        const val ALARM_SETTING_ITEM_EXTRA = "alarm_setting_item_extra"
        const val ALARM_SETTING_MODIFY_REQUEST_CODE = 100
        const val ALARM_SETTING_MODIFY_RESULT_CODE = -100
    }


}
