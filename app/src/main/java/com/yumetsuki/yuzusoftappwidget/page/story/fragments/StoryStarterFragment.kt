package com.yumetsuki.yuzusoftappwidget.page.story.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryActViewModel
import com.yumetsuki.yuzusoftappwidget.page.story.viewmodel.StoryStarterViewModel
import kotlinx.android.synthetic.main.fragment_story_starter.view.*

class StoryStarterFragment : Fragment() {

    companion object {
        fun newInstance() = StoryStarterFragment()
    }

    private val viewModel: StoryStarterViewModel by lazy {
        ViewModelProviders.of(this).get(StoryStarterViewModel::class.java)
    }

    private val actViewModel: StoryActViewModel by lazy {
        ViewModelProviders.of(this.activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application))
            .get(StoryActViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_starter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            mStartNewStoryBtn.setOnClickListener {
                actViewModel.requestFirstChapterId()
            }
        }
    }

}
