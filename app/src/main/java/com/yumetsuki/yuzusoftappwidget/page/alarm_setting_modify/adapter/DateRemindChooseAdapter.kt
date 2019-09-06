package com.yumetsuki.yuzusoftappwidget.page.alarm_setting_modify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.DateChosen
import kotlinx.android.synthetic.main.date_remind_choose_item.view.*

class DateRemindChooseAdapter(
    private val dateChosens: MutableLiveData<ArrayList<DateChosen>>,
    private val onDateChosen: (position: Int, isEnable: Boolean) -> Unit
): RecyclerView.Adapter<DateRemindChooseAdapter.DateRemindChooseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateRemindChooseViewHolder {
        return DateRemindChooseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.date_remind_choose_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dateChosens.value!!.size
    }

    override fun onBindViewHolder(holder: DateRemindChooseViewHolder, position: Int) {
        holder.bind(dateChosens.value!![position])
    }

    inner class DateRemindChooseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(dateChosen: DateChosen) {
            itemView.apply {
                mDateRemindChooseText.text = dateChosen.text
                mDateRemindChooseCheckBox.setOnCheckedChangeListener(null)
                mDateRemindChooseCheckBox.isChecked = dateChosen.isEnable
                mDateRemindChooseCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    onDateChosen(layoutPosition, isChecked)
                }
            }
        }

    }
}