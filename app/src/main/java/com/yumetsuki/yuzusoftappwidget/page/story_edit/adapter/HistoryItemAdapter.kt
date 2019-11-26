package com.yumetsuki.yuzusoftappwidget.page.story_edit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.SimpleHistory
import kotlinx.android.synthetic.main.item_history_page.view.*

class HistoryItemAdapter(
    private val histories: MutableLiveData<List<SimpleHistory>>,
    private val onBackIconClick: (simpleHistory: SimpleHistory, index: Int) -> Unit,
    private val onRemoveIconClick: (simpleHistory: SimpleHistory, index: Int) -> Unit
): RecyclerView.Adapter<HistoryItemAdapter.HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history_page, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return histories.value?.size?:0
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(histories.value!![position])
    }

    inner class HistoryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(simpleHistory: SimpleHistory) {
            itemView.apply {
                mSpeakerNameTv.text = simpleHistory.name
                mSpeakerContentTv.text = simpleHistory.content
                mBackHistoryBtn.setOnClickListener {
                    onBackIconClick(simpleHistory, adapterPosition)
                }
                mRemoveHistoryBtn.setOnClickListener {
                    onRemoveIconClick(simpleHistory, adapterPosition)
                }
            }
        }
    }
}