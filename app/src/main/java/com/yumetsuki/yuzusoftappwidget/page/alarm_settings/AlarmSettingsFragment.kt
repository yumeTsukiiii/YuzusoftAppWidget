package com.yumetsuki.yuzusoftappwidget.page.alarm_settings

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Alarm
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify.AlarmSettingModifyActivity
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter.AlarmSettingsAdapter
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.viewmodel.AlarmSettingsViewModel
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSettingDays
import kotlinx.android.synthetic.main.fragment_alarm_settings.view.*
import java.util.*
import kotlin.random.Random

class AlarmSettingsFragment: Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.activity!!.application))
            .get(AlarmSettingsViewModel::class.java)
    }

    private var backgroundImageResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundImageResource = arguments!!.getInt(ALARM_SETTINGS_BACKGROUND_ARGUMENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_settings, container, false).apply {

            (activity!! as AppCompatActivity).apply {
                setSupportActionBar(mAlarmSettingsToolbar)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }

            setHasOptionsMenu(true)

            initViewModel()

            initView()

            initTextFont()
        }
    }

    /**
     * 设置字体样式
     * */
    private fun View.initTextFont() {
        mNoAlarmTipText.typeface = Typeface.createFromAsset(context.assets, "fonts/简哈哈.ttf")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_alarm_settings_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_alarm_item -> {
                startActivityForResult(Intent(
                    context,
                    AlarmSettingModifyActivity::class.java
                ).apply {
                    putExtra(
                        AlarmSettingModifyActivity.ALARM_SETTING_ITEM_EXTRA,
                        AlarmSettingModel(
                            AlarmSetting(
                                viewModel.newAlarmId,
                                Alarm.values()[Random.nextInt(0, Alarm.values().size)].imageResource,
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                Calendar.getInstance().get(Calendar.MINUTE),
                                false
                            ),
                            listOf(AlarmSettingDays(-1, 1, -1))
                        )
                    )
                }, AlarmSettingModifyActivity.ALARM_SETTING_MODIFY_REQUEST_CODE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            AlarmSettingModifyActivity.ALARM_SETTING_MODIFY_REQUEST_CODE -> {
                handleModifiedAlarmModel(
                    data?.getParcelableExtra<AlarmSettingModel>(
                        AlarmSettingModifyActivity.ALARM_SETTING_ITEM_EXTRA
                    )
                )
            }
        }
    }

    private fun handleModifiedAlarmModel(alarmSettingModel: AlarmSettingModel?) {
        alarmSettingModel?.also {
            if (it.alarmSetting.id == viewModel.newAlarmId) {
                viewModel.newAlarm(it)
            } else {
                viewModel.updateAlarm(alarmSettingModel)
            }
        }
    }

    private fun View.initViewModel() {

        viewModel.alarmSettings.value = arrayListOf()

        viewModel.alarmSettings.observe(this@AlarmSettingsFragment, Observer {
            mNoAlarmTipText.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            viewModel.alarmSettingsAdapter?.notifyDataSetChanged()
        })
    }

    private fun View.initView() {
        mAlarmSettingsRecyclerView.adapter = AlarmSettingsAdapter(
            viewModel.alarmSettings,
            onAlarmToggleIconClick = {
                viewModel.toggleAlarm(it)
            },
            onAlarmItemClick = {
                startActivityForResult(Intent(
                    context,
                    AlarmSettingModifyActivity::class.java
                ).apply {
                    putExtra(
                        AlarmSettingModifyActivity.ALARM_SETTING_ITEM_EXTRA,
                        it
                    )
                }, AlarmSettingModifyActivity.ALARM_SETTING_MODIFY_REQUEST_CODE)
            },
            onAlarmRemoveIconClick = {
                viewModel.deleteAlarm(it)
            }
        ).apply { viewModel.alarmSettingsAdapter = this }

        mAlarmSettingsRecyclerView.layoutManager = LinearLayoutManager(this@AlarmSettingsFragment.context)

        mAlarmSettingsFragmentLayout
            .background = resources.getDrawable(backgroundImageResource!!, resources.newTheme())

    }

    companion object {

        private const val ALARM_SETTINGS_BACKGROUND_ARGUMENT = "alarm_settings_background_argument"

        fun newInstance(backgroundImageResource: Int): AlarmSettingsFragment {
            return AlarmSettingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALARM_SETTINGS_BACKGROUND_ARGUMENT, backgroundImageResource)
                }
            }
        }
    }
}