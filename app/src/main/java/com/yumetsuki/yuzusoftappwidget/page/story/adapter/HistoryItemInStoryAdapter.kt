package com.yumetsuki.yuzusoftappwidget.page.story.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.SimpleHistory
import kotlinx.android.synthetic.main.item_story_history_page.view.*

class HistoryItemInStoryAdapter(
    private val histories: MutableLiveData<List<SimpleHistory>>,
    private val onBackIconClick: (simpleHistory: SimpleHistory, index: Int) -> Unit
): RecyclerView.Adapter<HistoryItemInStoryAdapter.HistoryItemInStoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemInStoryViewHolder {
        return HistoryItemInStoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_history_page, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return histories.value?.size?:0
    }

    override fun onBindViewHolder(holder: HistoryItemInStoryViewHolder, position: Int) {
        holder.bind(histories.value!![position])
    }

    inner class HistoryItemInStoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(simpleHistory: SimpleHistory) {
            itemView.apply {
                mSpeakerNameTv.text = simpleHistory.name
                mSpeakerContentTv.text = simpleHistory.content
                mBackHistoryBtn.setOnClickListener {
                    onBackIconClick(simpleHistory, adapterPosition)
                }
            }
        }
    }
}