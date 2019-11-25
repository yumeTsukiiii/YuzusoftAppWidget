package com.yumetsuki.yuzusoftappwidget.page.story

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yumetsuki.yuzusoftappwidget.R

class StoryFragment : Fragment() {

    companion object {
        fun newInstance() = StoryFragment()
    }

    private val viewModel: StoryViewModel by lazy {
        ViewModelProviders.of(this).get(StoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.story_fragment, container, false)
    }

}
