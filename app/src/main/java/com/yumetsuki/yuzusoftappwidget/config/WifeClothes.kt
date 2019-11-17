package com.yumetsuki.yuzusoftappwidget.config

import com.yumetsuki.yuzusoftappwidget.R

/**
 * 老婆衣服配置，会对应触发的语音
 * */
enum class WifeClothes(
    val clothesName: String,
    val voiceMap: List<Pair<Int, Int>>, //first为立绘资源，second为音频资源
    val expressions: List<Int>,
    val normalImage: Int
) {

    YoshinoWitch("巫女服", listOf(
        R.drawable.yoshino_hatsukashi to R.raw.yos_touch_1,
        R.drawable.yoshino_smile_onegai to R.raw.yos_touch_2,
        R.drawable.yoshino_ogodda to R.raw.yos_touch_3,
        R.drawable.yoshino_ogodda to R.raw.yos_touch_4,
        R.drawable.yoshino_hatsukashi to R.raw.yos_touch_5
    ), listOf(
        R.drawable.yoshino_normal,
        R.drawable.yoshino_hatsukashi,
        R.drawable.yoshino_smile_onegai,
        R.drawable.yoshino_ogodda
    ), R.drawable.yoshino_normal
    ),

    YoshinoSeifuku("学院制服", listOf(
        R.drawable.yoshino_hatsukashi_seifuku to R.raw.yos_touch_1,
        R.drawable.yoshino_smile_seifuku_onegai to R.raw.yos_touch_2,
        R.drawable.yoshino_ogodda_seifuku to R.raw.yos_touch_3,
        R.drawable.yoshino_ogodda_seifuku to R.raw.yos_touch_4,
        R.drawable.yoshino_hatsukashi_seifuku to R.raw.yos_touch_5
    ), listOf(
        R.drawable.yoshino_seifuku_normal,
        R.drawable.yoshino_hatsukashi_seifuku,
        R.drawable.yoshino_smile_seifuku_onegai,
        R.drawable.yoshino_ogodda_seifuku
    ), R.drawable.yoshino_seifuku_normal
    ),

    MakoNinja("忍者服", listOf(
        R.drawable.mako_smile_front to R.raw.mak_touch_1,
        R.drawable.mako_yonbiri_front to R.raw.mak_touch_2,
        R.drawable.mako_yonbiri_back to R.raw.mak_touch_2,
        R.drawable.mako_yonbiri_front to R.raw.mak_touch_3,
        R.drawable.mako_yonbiri_back to R.raw.mak_touch_3
    ), listOf(
        R.drawable.mako_normal,
        R.drawable.mako_smile_front,
        R.drawable.mako_yonbiri_front,
        R.drawable.mako_yonbiri_back
    ), R.drawable.mako_normal
    ),

    MakoSeifuku("学院制服", listOf(
        R.drawable.mako_smile_front_seifuku to R.raw.mak_touch_1,
        R.drawable.mako_yonbiri_seifuku_front to R.raw.mak_touch_2,
        R.drawable.mako_yonbiri_seifuku_back to R.raw.mak_touch_2,
        R.drawable.mako_yonbiri_seifuku_front to R.raw.mak_touch_3,
        R.drawable.mako_yonbiri_seifuku_back to R.raw.mak_touch_3
    ), listOf(
        R.drawable.mako_normal_seifuku,
        R.drawable.mako_smile_front_seifuku,
        R.drawable.mako_yonbiri_seifuku_front,
        R.drawable.mako_yonbiri_seifuku_back
    ), R.drawable.mako_normal_seifuku
    ),

    MurasameWitch("巫女服", listOf(
        R.drawable.murasame_smile_hatsukashi to R.raw.mur_touch_1,
        R.drawable.murasame_bikuri to R.raw.mur_touch_2,
        R.drawable.murasame_ogodda to R.raw.mur_touch_3,
        R.drawable.murasame_ogodda_front to R.raw.mur_touch_4,
        R.drawable.murasame_bikuri to R.raw.mur_touch_5,
        R.drawable.murasame_bikuri_front to R.raw.mur_touch_5
    ), listOf(
        R.drawable.murasame_normal,
        R.drawable.murasame_smile_hatsukashi,
        R.drawable.murasame_bikuri,
        R.drawable.murasame_ogodda,
        R.drawable.murasame_ogodda_front,
        R.drawable.murasame_bikuri_front
    ), R.drawable.murasame_normal
    ),

    MurasameSeifuku("学院制服", listOf(
        R.drawable.murasame_smile_hatsukashi_seifuku to R.raw.mur_touch_1,
        R.drawable.murasame_bikuri_seifuku to R.raw.mur_touch_2,
        R.drawable.murasame_ogodda_seifuku to R.raw.mur_touch_3,
        R.drawable.murasame_ogodda_seifuku_front to R.raw.mur_touch_4,
        R.drawable.murasame_bikuri_seifuku to R.raw.mur_touch_5,
        R.drawable.murasame_bikuri_seifuku_front to R.raw.mur_touch_5
    ), listOf(
        R.drawable.murasame_normal_seifuku,
        R.drawable.murasame_bikuri_seifuku,
        R.drawable.murasame_ogodda_seifuku,
        R.drawable.murasame_ogodda_seifuku_front,
        R.drawable.murasame_bikuri_seifuku_front
    ), R.drawable.murasame_normal_seifuku
    ),

    AtsusaSeifuku("学院制服", listOf(
        R.drawable.atsusa_kodomo_01 to R.raw.az_touch_01,
        R.drawable.atsusa_kodomo_02_front to R.raw.az_touch_01,
        R.drawable.atsusa_question_front to R.raw.az_touch_02,
        R.drawable.atsusa_bikuri_front to R.raw.az_touch_03
    ), listOf(
        R.drawable.atsusa_normal,
        R.drawable.atsusa_kodomo_01,
        R.drawable.atsusa_kodomo_02_front,
        R.drawable.atsusa_question_front,
        R.drawable.atsusa_bikuri_front
    ), R.drawable.atsusa_normal
    ),
    AtsusaCommon("常服", listOf(
        R.drawable.atsusa_kodomo_common_01 to R.raw.az_touch_01,
        R.drawable.atsusa_kodomo_common_02_front to R.raw.az_touch_01,
        R.drawable.atsusa_question_common_front to R.raw.az_touch_02,
        R.drawable.atsusa_bikuri_common_front to R.raw.az_touch_03
    ), listOf(
        R.drawable.atsusa_common_normal,
        R.drawable.atsusa_kodomo_common_01,
        R.drawable.atsusa_kodomo_common_02_front,
        R.drawable.atsusa_question_common_front,
        R.drawable.atsusa_bikuri_common_front
    ), R.drawable.atsusa_common_normal
    )
}