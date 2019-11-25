package com.yumetsuki.yuzusoftappwidget.page.story_edit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.repo.entity.StoryChapter
import kotlinx.android.synthetic.main.item_story_chapter.view.*

class ChapterEditItemAdapter(
    private val chapters: MutableLiveData<ArrayList<StoryChapter>>,
    private val onRemoveIconClick: (chapter: StoryChapter) -> Unit,
    private val onChapterClick: (chapter: StoryChapter, index: Int) -> Unit
): RecyclerView.Adapter<ChapterEditItemAdapter.ChapterEditItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterEditItemViewHolder {
        return ChapterEditItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_story_chapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return chapters.value?.size?:0
    }

    override fun onBindViewHolder(holder: ChapterEditItemViewHolder, position: Int) {
        holder.bind(chapters.value!![position])
    }


    inner class ChapterEditItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(chapter: StoryChapter) {
            itemView.apply {
                mChapterNameTv.text = chapter.chapterName
                setOnClickListener {
                    onChapterClick(chapter, adapterPosition)
                }
                mRemoveChapterBtn.setOnClickListener {
                    onRemoveIconClick(chapter)
                }
            }
        }
    }
}