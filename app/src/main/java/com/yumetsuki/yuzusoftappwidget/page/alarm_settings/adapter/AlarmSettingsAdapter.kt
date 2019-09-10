package com.yumetsuki.yuzusoftappwidget.page.alarm_settings.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.AlarmSettingModel
import com.yumetsuki.yuzusoftappwidget.utils.formatDaysText
import com.yumetsuki.yuzusoftappwidget.utils.formatTimeText
import kotlinx.android.synthetic.main.alarm_setting_item.view.*
import java.util.*

class AlarmSettingsAdapter(
    private val alarmSettings: MutableLiveData<ArrayList<AlarmSettingModel>>,
    private val onAlarmToggleIconClick: (alarmSetting: AlarmSettingModel) -> Unit,
    private val onAlarmItemClick: (alarmSetting: AlarmSettingModel) -> Unit,
    private val onAlarmRemoveIconClick: (alarmSetting: AlarmSettingModel) -> Unit
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

                initTextFont()

                mAlarmImage.setImageResource(alarmSetting.alarmSetting.icon)

                mAlarmTimeText.text = buildTimeText(alarmSetting)

                mAlarmDayText.text = buildDaysText(alarmSetting)

                mAlarmToggleControl.setImageResource(
                    if (alarmSetting.alarmSetting.isEnable) {
                        R.drawable.ic_alarm_on_pink_24dp
                    } else {
                        R.drawable.ic_alarm_off_pink_24dp
                    }
                )

                mAlarmToggleControl.setOnClickListener {
                    onAlarmToggleIconClick(alarmSetting)
                }

                mAlarmRemoveControl.setOnClickListener {
                    onAlarmRemoveIconClick(alarmSetting)
                }

                mAlarmSettingsItemLayout.setOnClickListener {
                    onAlarmItemClick(alarmSetting)
                }
            }
        }

    }

    private fun View.initTextFont() {
        mAlarmTimeText.typeface = Typeface.createFromAsset(context.assets, "fonts/简哈哈.ttf")
    }

    private fun buildTimeText(alarmSetting: AlarmSettingModel): String {
        return alarmSetting.formatTimeText()
    }

    private fun buildDaysText(alarmSetting: AlarmSettingModel): String {
        return alarmSetting.formatDaysText()
    }
}