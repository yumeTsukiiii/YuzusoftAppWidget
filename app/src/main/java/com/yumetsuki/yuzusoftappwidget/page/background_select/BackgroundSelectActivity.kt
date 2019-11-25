package com.yumetsuki.yuzusoftappwidget.page.background_select

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Background
import com.yumetsuki.yuzusoftappwidget.page.background_select.adapter.BackgroundSelectAdapter
import com.yumetsuki.yuzusoftappwidget.page.background_select.viewmodel.BackgroundSelectViewModel
import kotlinx.android.synthetic.main.activity_background_select.*

class BackgroundSelectActivity : AppCompatActivity() {

    private val viewModel: BackgroundSelectViewModel by lazy {
        ViewModelProvider(this).get(BackgroundSelectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_select)
        setSupportActionBar(mBackgroundSelectToolbar)

        viewModel.selectedBackgroundIndex.observe(this, Observer {
            (mBackgroundSelectRecyclerView.adapter as? BackgroundSelectAdapter)?.apply {
                updateBackgrounds(
                    Background.values().mapIndexed { index, background ->
                        background to (index == it)
                    }.toTypedArray()
                )
            }
        })

        mBackgroundSelectRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mBackgroundSelectRecyclerView.adapter = BackgroundSelectAdapter(
            Background.values().mapIndexed { index, background ->
                background to (index == viewModel.selectedBackgroundIndex.value)
            }.toTypedArray()
        ) { index, _ ->
            viewModel.selectedBackgroundIndex.value = index
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_background_select_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_complete_select_background -> {
                setResult(FOR_BACKGROUND_SELECT_RESULT, Intent().apply {
                    putExtra(SELECT_BACKGROUND, Background.values()[viewModel.selectedBackgroundIndex.value!!])
                })
                finish()
            }
        }
        return true
    }

    companion object {
        const val SELECT_BACKGROUND = "select_background"
        const val FOR_BACKGROUND_SELECT_RESULT = 2001
    }
}
