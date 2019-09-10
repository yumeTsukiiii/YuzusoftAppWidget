package com.yumetsuki.yuzusoftappwidget.page.main.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yumetsuki.yuzusoftappwidget.*
import com.yumetsuki.yuzusoftappwidget.app_widget.YuzusoftAppWidgetProvider
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.config.WifeClothes
import com.yumetsuki.yuzusoftappwidget.utils.applicationPref
import com.yumetsuki.yuzusoftappwidget.utils.playMediaAsync

class CharacterFragmentViewModel: ViewModel() {

    val wife = MutableLiveData<Wife>()

    val isMusicPlaying = MutableLiveData<Boolean>()

    val previousClothesIndex = MutableLiveData<Int>()

    val chosenClothesIndex = MutableLiveData<Int>()

    var musicPlayer: MediaPlayer? = null

    val clothes = MutableLiveData<List<WifeClothes>>()

    fun updateWifeClothes(context: Context) {
        AppContext.applicationPref()
            .putWifeClothes(
                wife.value!!.wifeName,
                clothes.value!![chosenClothesIndex.value!!].clothesName
            )
        updateWidget(context)
    }

    fun startPlayCharacterMusic(context: Context?) {
        musicPlayer = MediaPlayer.create(context, wife.value!!.characterMusic).apply {
            start()
        }
    }

    fun stopPlayCharacterMusic() {
        musicPlayer?.apply {
            stop()
            release()
            musicPlayer = null
        }
    }

    fun updateMostLikeWife(context: Context) {
        CharacterConfig.mostLikeWifeName = wife.value!!.wifeName
        updateWidget(context)
    }

    private fun updateWidget(context: Context) {
        context.sendBroadcast(Intent(YuzusoftAppWidgetProvider.UPDATE_ACTION).apply {
            component = ComponentName(
                context,
                YuzusoftAppWidgetProvider::class.java
            )
        })
    }

}