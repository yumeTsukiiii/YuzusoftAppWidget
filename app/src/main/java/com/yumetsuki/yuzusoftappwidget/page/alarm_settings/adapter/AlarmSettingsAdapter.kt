package com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.repo.entity.AlarmSetting
import kotlinx.android.synthetic.main.alarm_setting_item.view.*
import java.util.*

class AlarmSettingsAdapter(
    private val alarmSettings: MutableLiveData<ArrayList<AlarmSettingModel>>,
    private val onAlarmIconClick: (alarmSetting: AlarmSettingModel) -> Unit,
    private val onAlarmItemClick: (alarmSetting: AlarmSettingModel) -> Unit
): RecyclerView.Adapter<AlarmSettingsAdapter.AlarmSettingsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSettingsViewHolder {
        return AlarmSettingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.alarm_setting_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return alarmSettings.value!!.size
    }

    override fun onBindViewHolder(holder: AlarmSettingsViewHolder, position: Int) {
        holder.bind(alarmSettings.value!![position])
    }

    inner class AlarmSettingsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(alarmSetting: AlarmSettingModel) {
            itemView.apply {
                mAlarmImage.setImageResource(alarmSetting.alarmSetting.icon)

                mAlarmTimeText.text = buildTimeText(alarmSetting)

                mAlarmDayText.text = buildDaysText(alarmSetting)

                mAlarmToggleControl.setOnClickListener {
                    onAlarmIconClick(alarmSetting)
                }

                mAlarmSettingsItemLayout.setOnClickListener {
                    onAlarmItemClick(alarmSetting)
                }
            }
        }

    }

    private fun buildTimeText(alarmSetting: AlarmSettingModel): String {
        return alarmSetting.alarmSetting.run {
            "${
            if (alarmHour > 12) {
                alarmHour - 12
            } else {
                alarmHour
            }
            }:$alarmMinute ${
            if (alarmHour > 12) {
                "pm"
            } else {
                "am"
            }
            }"
        }
    }

    private fun buildDaysText(alarmSetting: AlarmSettingModel): String {
        return if (
            alarmSetting.days.size == 2
            && alarmSetting.days.map { it.day }.containsAll(listOf(6, 7))
        ) {
            "双休"
        } else if (
            alarmSetting.days.size == 7
        ) {
            "每天"
        } else {
            alarmSetting.days.joinToString("、") {
                when(it.day) {
                    1 -> {
                        "周一"
                    }
                    2 -> {
                        "周二"
                    }
                    3 -> {
                        "周三"
                    }
                    4 -> {
                        "周四"
                    }
                    5 -> {
                        "周五"
                    }
                    6 -> {
                        "周六"
                    }
                    7 -> {
                        "周日"
                    }
                    else -> {
                        ""
                    }
                }
            }
        }
    }
}