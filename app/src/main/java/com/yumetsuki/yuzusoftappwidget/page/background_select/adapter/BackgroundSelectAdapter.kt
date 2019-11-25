package com.yumetsuki.yuzusoftappwidget.page.background_select.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Background
import kotlinx.android.synthetic.main.item_background_select.view.*

class BackgroundSelectAdapter(
    private var backgrounds: Array<Pair<Background, Boolean>>,
    private val onBackgroundSelect: (index: Int, background: Background) -> Unit
): RecyclerView.Adapter<BackgroundSelectAdapter.CharacterSelectViewHolder>() {

    fun updateBackgrounds(backgrounds: Array<Pair<Background, Boolean>>) {
        this.backgrounds = backgrounds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterSelectViewHolder {
        return CharacterSelectViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_background_select, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return backgrounds.size
    }

    override fun onBindViewHolder(holder: CharacterSelectViewHolder, position: Int) {
        holder.bind(backgrounds[position])
    }


    inner class CharacterSelectViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(background: Pair<Background, Boolean>) {
            itemView.apply {
                mSelectBackgroundImage.setImageResource(background.first.res)
                mSelectBackgroundRadioBtn.isChecked = background.second
                setOnClickListener {
                    onBackgroundSelect(adapterPosition, background.first)
                }
            }
        }

    }
}