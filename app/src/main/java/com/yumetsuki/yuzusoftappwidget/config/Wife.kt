package com.yumetsuki.yuzusoftappwidget.config

import com.yumetsuki.yuzusoftappwidget.R

/**
 * 老婆配置
 * */
enum class Wife(
    val gameBelong: Game,
    val avatar: Int,
    val wifeName: String,
    val chineseTransName: String,
    val res: List<WifeClothes>,
    val timerVoice: WifeTimerVoice,
    val appEndVoice: Int,
    val characterMusic: Int
) {

    Yoshino(
        Game.Senrenbanka,
        R.drawable.yoshino_icon, "yoshino", "朝武芳乃",
        listOf(
            WifeClothes.YoshinoWitch,
            WifeClothes.YoshinoSeifuku
        ),
        WifeTimerVoice.Yoshino,
        R.raw.yos_end,
        R.raw.yoshino_character_music
    ),
    Mako(
        Game.Senrenbanka,
        R.drawable.mako_icon, "mako", "常陆茉子",
        listOf(
            WifeClothes.MakoNinja,
            WifeClothes.MakoSeifuku
        ),
        WifeTimerVoice.Mako,
        R.raw.mak_end,
        R.raw.mako_character_music
    ),
    Murasame(
        Game.Senrenbanka,
        R.drawable.murasame_icon, "murasame", "丛雨",
        listOf(
            WifeClothes.MurasameWitch,
            WifeClothes.MurasameSeifuku
        ),
        WifeTimerVoice.Murasame,
        R.raw.mur_end,
        R.raw.murasame_character_music
    ),
    Atsusa(
        Game.DracuRiot,
        R.drawable.atsusa_icon, "atsusa", "布良梓",
        listOf(
            WifeClothes.AtsusaSeifuku,
            WifeClothes.AtsusaCommon
        ),
        WifeTimerVoice.Atsusa,
        R.raw.az_end,
        R.raw.atsusa_character_music
    )
}