package com.yumetsuki.yuzusoftappwidget.page.character_select

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.yumetsuki.yuzusoftappwidget.R
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.page.character_select.adapter.CharacterSelectAdapter
import com.yumetsuki.yuzusoftappwidget.page.character_select.viewModel.CharacterSelectViewModel
import kotlinx.android.synthetic.main.activity_character_select.*

class CharacterSelectActivity : AppCompatActivity() {

    private val viewModel: CharacterSelectViewModel by lazy {
        ViewModelProvider(this).get(CharacterSelectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_select)
        setSupportActionBar(mCharacterSelectToolbar)

        viewModel.selectedWifeIndex.observe(this, Observer {
            (mCharacterSelectRecyclerView.adapter as? CharacterSelectAdapter)?.apply {
                updateWifes(
                    Wife.values().mapIndexed { index, wife ->
                        wife to (index == it)
                    }.toTypedArray()
                )
            }
        })

        mCharacterSelectRecyclerView.layoutManager = GridLayoutManager(this, 4)

        mCharacterSelectRecyclerView.adapter = CharacterSelectAdapter(
            Wife.values().mapIndexed { index, wife ->
                wife to (index == viewModel.selectedWifeIndex.value)
            }.toTypedArray()
        ) { index, _ ->
            viewModel.selectedWifeIndex.value = index
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_character_select_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_complete_select_character -> {
                setResult(FOR_WIFE_SELECT_RESULT, Intent().apply {
                    putExtra(SELECT_WIFE, Wife.values()[viewModel.selectedWifeIndex.value!!])
                })
                finish()
            }
        }
        return true
    }

    companion object {
        const val SELECT_WIFE = "select_wife"
        const val FOR_WIFE_SELECT_RESULT = 1001
    }

}
