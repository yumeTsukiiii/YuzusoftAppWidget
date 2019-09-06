package com.yumetsuki.yuzusoftappwidget

import android.content.SharedPreferences
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.utils.pref

object CharacterConfig {

    var mostLikeWife by pref(Wife.Yoshino.wifeName)

    const val clothesSufix = "-clothes"

}

fun SharedPreferences.getWifeClothesName(wifeName: String):String {
    return getString("$wifeName${CharacterConfig.clothesSufix}", "")!!
}

fun SharedPreferences.putWifeClothes(wifeName: String, clothesName: String) {
    edit().putString("$wifeName${CharacterConfig.clothesSufix}", clothesName)
        .apply()
}