package com.yumetsuki.yuzusoftappwidget.config

import com.yumetsuki.yuzusoftappwidget.R

enum class Wife(
    val gameBelong: Game,
    val avatar: Int,
    val wifeName: String,
    val chineseTransName: String,
    val res: List<WifeClothes>,
    val timerVoice: WifeTimerVoice
) {

    Yoshino(
        Game.Senrenbanka,
        R.drawable.yoshino_icon, "yoshino", "朝武芳乃",
        listOf(
            WifeClothes.YoshinoWitch,
            WifeClothes.YoshinoSeifuku
        ),
        WifeTimerVoice.Yoshino
    ),
    Mako(
        Game.Senrenbanka,
        R.drawable.mako_icon, "mako", "常陆茉子",
        listOf(
            WifeClothes.MakoNinja,
            WifeClothes.MakoSeifuku
        ),
        WifeTimerVoice.Mako
    ),
    Murasame(
        Game.Senrenbanka,
        R.drawable.murasame_icon, "murasame", "丛雨",
        listOf(
            WifeClothes.MurasameWitch,
            WifeClothes.MurasameSeifuku
        ),
        WifeTimerVoice.Murasame
    ),
    Atsusa(
        Game.DracuRiot,
        R.drawable.atsusa_icon, "atsusa", "布良梓",
        listOf(
            WifeClothes.AtsusaSeifuku
        ),
        WifeTimerVoice.Atsusa
    )
}