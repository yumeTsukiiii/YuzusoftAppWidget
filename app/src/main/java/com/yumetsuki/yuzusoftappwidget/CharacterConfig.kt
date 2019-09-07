package com.yumetsuki.yuzusoftappwidget

import android.content.SharedPreferences
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.config.WifeClothes
import com.yumetsuki.yuzusoftappwidget.utils.applicationPref
import com.yumetsuki.yuzusoftappwidget.utils.pref

object CharacterConfig {

    var mostLikeWifeName by pref(Wife.Yoshino.wifeName)

    const val clothesSufix = "-clothes"

}

/**
 * 获取当前选择的角色和该角色选择的衣服
 * */
fun CharacterConfig.getMostLikeWifeWithClothes(): Pair<Wife, WifeClothes> {
    return getMostLikeWife().let {
        it to AppContext.applicationPref().wifeCurrentClothes(it)
    }
}

fun CharacterConfig.getMostLikeWife(): Wife {
    return Wife.values().find { it.wifeName == mostLikeWifeName }?:Wife.Yoshino
}

fun SharedPreferences.wifeCurrentClothes(wifeName: String): WifeClothes {
    return Wife.values().find { it.wifeName == wifeName }?.run {
        res.find {
            (getWifeClothesName(wifeName)
                == it.clothesName)
        }?: error("no clothes")
    }?: error("no wife")
}

fun SharedPreferences.wifeCurrentClothes(wife: Wife): WifeClothes {
    return wife.run {
        res.find {
            (getWifeClothesName(wifeName)
                    == it.clothesName)
        }?: error("no clothes")
    }
}


fun SharedPreferences.getWifeClothesName(wifeName: String): String {
    return getString("$wifeName${CharacterConfig.clothesSufix}", "")!!
}

fun SharedPreferences.putWifeClothes(wifeName: String, clothesName: String) {
    edit().putString("$wifeName${CharacterConfig.clothesSufix}", clothesName)
        .apply()
}