package com.yumetsuki.yuzusoftappwidget.page.character_select.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Wife
import kotlinx.android.synthetic.main.item_character_select.view.*

class CharacterSelectAdapter(
    private var wifes: Array<Pair<Wife, Boolean>>,
    private val onCharacterSelect: (index: Int, wife: Wife) -> Unit
): RecyclerView.Adapter<CharacterSelectAdapter.CharacterSelectViewHolder>() {

    fun updateWifes(wifes: Array<Pair<Wife, Boolean>>) {
        this.wifes = wifes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterSelectViewHolder {
        return CharacterSelectViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character_select, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return wifes.size
    }

    override fun onBindViewHolder(holder: CharacterSelectViewHolder, position: Int) {
        holder.bind(wifes[position])
    }


    inner class CharacterSelectViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        fun bind(wife: Pair<Wife, Boolean>) {
            itemView.apply {
                mSelectCharacterIcon.setImageResource(wife.first.avatar)
                mSelectCharacterImage.setImageResource(wife.first.res[0].normalImage)
                mSelectCharacterRadioBtn.isChecked = wife.second
                mSelectCharacterImage.setOnClickListener {
                    onCharacterSelect(adapterPosition, wife.first)
                }
            }
        }

    }
}