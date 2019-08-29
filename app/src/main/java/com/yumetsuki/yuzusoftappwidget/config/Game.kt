package com.yumetsuki.yuzusoftappwidget.config

import com.yumetsuki.yuzusoftappwidget.R

enum class Game(
    val gameName: String,
    val tabImage: Int,
    val backgroundImage: Int
) {
    Senrenbanka(
        "Senrenbanka",
        R.drawable.senrenbbanka_tab,
        R.drawable.senrenbanka_background
    ),
    DracuRiot(
        "DracuRiot",
        R.drawable.dracu_riot_tab,
        R.drawable.dracu_riot_background
    )
}