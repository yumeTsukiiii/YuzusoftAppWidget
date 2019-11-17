package com.yumetsuki.yuzusoftappwidget.page.story_edit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yumetsuki.yuzusoftappwidget.R

class StoryEditFragment: Fragment() {

    companion object {

        fun newInstance(): StoryEditFragment {
            return StoryEditFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_edit, container, false)
    }

}