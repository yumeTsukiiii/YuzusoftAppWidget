package com.yumetsuki.yuzusoftappwidget.page.story_board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.repo.entity.Story
import kotlinx.android.synthetic.main.item_story_board.view.*

class StoryItemAdapter(
    private val stories: MutableLiveData<ArrayList<Story>>,
    private val isEditMode: MutableLiveData<Boolean>,
    private val onRemoveIconClick: (story: Story) -> Unit,
    private val onStoryItemClick: (story: Story) -> Unit
): RecyclerView.Adapter<StoryItemAdapter.StoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryItemViewHolder {
        return StoryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_board, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return stories.value?.size?:0
    }

    override fun onBindViewHolder(holder: StoryItemViewHolder, position: Int) {
        holder.bind(stories.value!![position])
    }


    inner class StoryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(story: Story) {
            itemView.apply {
                mStoryNameTv.text = story.storyName
                mRemoveStoryBtn.visibility = isEditMode.value?.let { if(it) View.VISIBLE else View.GONE }?:View.GONE
                mRemoveStoryBtn.setOnClickListener {
                    onRemoveIconClick(story)
                }
            }
            itemView.setOnClickListener {
                onStoryItemClick(story)
            }
        }

    }
}