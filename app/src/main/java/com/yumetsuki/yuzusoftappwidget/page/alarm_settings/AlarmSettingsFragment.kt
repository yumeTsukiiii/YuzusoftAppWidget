package com.yumetsuki.yuzusoftappwidget.page.alarm_settings

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify.AlarmSettingModifyActivity
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter.AlarmSettingsAdapter
import com.yumetsuki.yuzusoftappwidget.page.alarm_settings.viewmodel.AlarmSettingsViewModel
import kotlinx.android.synthetic.main.fragment_alarm_settings.view.*

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

            mAlarmSettingsRecyclerView.adapter = AlarmSettingsAdapter(
                viewModel.alarmSettings,
                onAlarmIconClick = {

                },
                onAlarmItemClick = {

                }
            ).apply { viewModel.alarmSettingsAdapter = this }

            mAlarmSettingsFragmentLayout
                .background = resources.getDrawable(backgroundImageResource!!, resources.newTheme())

        }
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
                ), AlarmSettingModifyActivity.ALARM_SETTING_MODIFY_REQUEST_CODE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun View.initViewModel() {
        viewModel.alarmSettings.observe(this@AlarmSettingsFragment, Observer {
            mNoAlarmTipText.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            viewModel.alarmSettingsAdapter?.notifyDataSetChanged()
        })
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