package com.yumetsuki.yuzusoftappwidget.page.story_data.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.model.StoryPageModel
import kotlinx.android.synthetic.main.item_story_record_data.view.*

class StoryRecordDataItemAdapter(
    private val storyPageModels: MutableLiveData<ArrayList<Triple<Int, StoryPageModel, Boolean>>>,
    private val onItemClick: (storyPageModel: StoryPageModel, index: Int) -> Unit,
    private val onRemoveIconClick: (storyPageModel: StoryPageModel, index: Int) -> Unit
): RecyclerView.Adapter<StoryRecordDataItemAdapter.StoryRecordDataItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryRecordDataItemViewHolder {
        return StoryRecordDataItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_record_data, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return storyPageModels.value?.size?:0
    }

    override fun onBindViewHolder(holder: StoryRecordDataItemViewHolder, position: Int) {
        holder.bind(storyPageModels.value!![position])
    }


    inner class StoryRecordDataItemViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(storyPageModel: Triple<Int, StoryPageModel, Boolean>) {
            itemView.apply {
                mStoryRecordDataItemBackground.setImageResource(storyPageModel.second.background)
                if (storyPageModel.third) {
                    mStoryRecordDataItemBackground.setColorFilter(Color.parseColor("#662196F3"))
                }

                setOnClickListener {
                    onItemClick(storyPageModel.second, adapterPosition)
                }

                mStoryRecordRemoveBtn.visibility = if (storyPageModel.third) View.VISIBLE else View.GONE

                mStoryRecordRemoveBtn.setOnClickListener {
                    onRemoveIconClick(storyPageModel.second, adapterPosition)
                }
            }
        }

    }
}