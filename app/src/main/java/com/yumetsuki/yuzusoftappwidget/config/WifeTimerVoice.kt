package com.yumetsuki.yuzusoftappwidget.config

import com.yumetsuki.yuzusoftappwidget.R

/**
 * 老婆语音报时配置
 * */
enum class WifeTimerVoice(
    val morning: Int,
    val afternoon: Int,
    val hours: List<Int>,
    val minutes: List<List<Int>>,
    val desu: Int
) {

    Yoshino(
        R.raw.yos_watch_01,
        R.raw.yos_watch_02,
        listOf(
            R.raw.yos_watch_03,
            R.raw.yos_watch_04,
            R.raw.yos_watch_05,
            R.raw.yos_watch_06,
            R.raw.yos_watch_07,
            R.raw.yos_watch_08,
            R.raw.yos_watch_09,
            R.raw.yos_watch_10,
            R.raw.yos_watch_11,
            R.raw.yos_watch_12,
            R.raw.yos_watch_13,
            R.raw.yos_watch_14
        ),
        listOf(
            listOf(R.raw.yos_watch_20),
            listOf(R.raw.yos_watch_21),
            listOf(R.raw.yos_watch_22),
            listOf(R.raw.yos_watch_23),
            listOf(R.raw.yos_watch_24),
            listOf(R.raw.yos_watch_25),
            listOf(R.raw.yos_watch_26),
            listOf(R.raw.yos_watch_27),
            listOf(R.raw.yos_watch_28),
            listOf(R.raw.yos_watch_29),
            listOf(R.raw.yos_watch_30),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_21
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_22
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_23
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_24
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_25
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_26
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_27
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_28
            ),
            listOf(
                R.raw.yos_watch_15,
                R.raw.yos_watch_29
            ),
            listOf(R.raw.yos_watch_31),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_21
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_22
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_23
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_24
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_25
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_26
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_27
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_28
            ),
            listOf(
                R.raw.yos_watch_16,
                R.raw.yos_watch_29
            ),
            listOf(R.raw.yos_watch_32),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_21
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_22
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_23
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_24
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_25
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_26
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_27
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_28
            ),
            listOf(
                R.raw.yos_watch_17,
                R.raw.yos_watch_29
            ),
            listOf(R.raw.yos_watch_33),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_21
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_22
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_23
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_24
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_25
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_26
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_27
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_28
            ),
            listOf(
                R.raw.yos_watch_18,
                R.raw.yos_watch_29
            ),
            listOf(R.raw.yos_watch_34),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_21
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_22
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_23
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_24
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_25
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_26
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_27
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_28
            ),
            listOf(
                R.raw.yos_watch_19,
                R.raw.yos_watch_29
            )
        ),
        R.raw.yos_watch_35
    ),

    Mako(
        R.raw.mak_watch_01,
        R.raw.mak_watch_02,
        listOf(
            R.raw.mak_watch_03,
            R.raw.mak_watch_04,
            R.raw.mak_watch_05,
            R.raw.mak_watch_06,
            R.raw.mak_watch_07,
            R.raw.mak_watch_08,
            R.raw.mak_watch_09,
            R.raw.mak_watch_10,
            R.raw.mak_watch_11,
            R.raw.mak_watch_12,
            R.raw.mak_watch_13,
            R.raw.mak_watch_14
        ),
        listOf(
            listOf(R.raw.mak_watch_20),
            listOf(R.raw.mak_watch_21),
            listOf(R.raw.mak_watch_22),
            listOf(R.raw.mak_watch_23),
            listOf(R.raw.mak_watch_24),
            listOf(R.raw.mak_watch_25),
            listOf(R.raw.mak_watch_26),
            listOf(R.raw.mak_watch_27),
            listOf(R.raw.mak_watch_28),
            listOf(R.raw.mak_watch_29),
            listOf(R.raw.mak_watch_30),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_21
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_22
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_23
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_24
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_25
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_26
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_27
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_28
            ),
            listOf(
                R.raw.mak_watch_15,
                R.raw.mak_watch_29
            ),
            listOf(R.raw.mak_watch_31),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_21
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_22
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_23
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_24
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_25
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_26
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_27
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_28
            ),
            listOf(
                R.raw.mak_watch_16,
                R.raw.mak_watch_29
            ),
            listOf(R.raw.mak_watch_32),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_21
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_22
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_23
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_24
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_25
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_26
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_27
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_28
            ),
            listOf(
                R.raw.mak_watch_17,
                R.raw.mak_watch_29
            ),
            listOf(R.raw.mak_watch_33),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_21
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_22
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_23
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_24
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_25
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_26
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_27
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_28
            ),
            listOf(
                R.raw.mak_watch_18,
                R.raw.mak_watch_29
            ),
            listOf(R.raw.mak_watch_34),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_21
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_22
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_23
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_24
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_25
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_26
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_27
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_28
            ),
            listOf(
                R.raw.mak_watch_19,
                R.raw.mak_watch_29
            )
        ),
        R.raw.mak_watch_35
    ),

    Murasame(
        R.raw.mur_watch_01,
        R.raw.mur_watch_02,
        listOf(
            R.raw.mur_watch_03,
            R.raw.mur_watch_04,
            R.raw.mur_watch_05,
            R.raw.mur_watch_06,
            R.raw.mur_watch_07,
            R.raw.mur_watch_08,
            R.raw.mur_watch_09,
            R.raw.mur_watch_10,
            R.raw.mur_watch_11,
            R.raw.mur_watch_12,
            R.raw.mur_watch_13,
            R.raw.mur_watch_14
        ),
        listOf(
            listOf(R.raw.mur_watch_20),
            listOf(R.raw.mur_watch_21),
            listOf(R.raw.mur_watch_22),
            listOf(R.raw.mur_watch_23),
            listOf(R.raw.mur_watch_24),
            listOf(R.raw.mur_watch_25),
            listOf(R.raw.mur_watch_26),
            listOf(R.raw.mur_watch_27),
            listOf(R.raw.mur_watch_28),
            listOf(R.raw.mur_watch_29),
            listOf(R.raw.mur_watch_30),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_21
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_22
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_23
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_24
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_25
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_26
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_27
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_28
            ),
            listOf(
                R.raw.mur_watch_15,
                R.raw.mur_watch_29
            ),
            listOf(R.raw.mur_watch_31),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_21
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_22
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_23
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_24
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_25
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_26
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_27
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_28
            ),
            listOf(
                R.raw.mur_watch_16,
                R.raw.mur_watch_29
            ),
            listOf(R.raw.mur_watch_32),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_21
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_22
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_23
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_24
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_25
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_26
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_27
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_28
            ),
            listOf(
                R.raw.mur_watch_17,
                R.raw.mur_watch_29
            ),
            listOf(R.raw.mur_watch_33),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_21
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_22
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_23
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_24
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_25
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_26
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_27
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_28
            ),
            listOf(
                R.raw.mur_watch_18,
                R.raw.mur_watch_29
            ),
            listOf(R.raw.mur_watch_34),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_21
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_22
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_23
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_24
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_25
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_26
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_27
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_28
            ),
            listOf(
                R.raw.mur_watch_19,
                R.raw.mur_watch_29
            )
        ),
        R.raw.mur_watch_35
    ),

    Atsusa(
        R.raw.az_clock_am,
        R.raw.az_clock_pm,
        listOf(
            R.raw.az_clock_h_01,
            R.raw.az_clock_h_02,
            R.raw.az_clock_h_03,
            R.raw.az_clock_h_04,
            R.raw.az_clock_h_05,
            R.raw.az_clock_h_06,
            R.raw.az_clock_h_07,
            R.raw.az_clock_h_08,
            R.raw.az_clock_h_09,
            R.raw.az_clock_h_10,
            R.raw.az_clock_h_11,
            R.raw.az_clock_h_12
        ),
        listOf(
            listOf(R.raw.az_clock_m_00),
            listOf(R.raw.az_clock_m_01),
            listOf(R.raw.az_clock_m_02),
            listOf(R.raw.az_clock_m_03),
            listOf(R.raw.az_clock_m_04),
            listOf(R.raw.az_clock_m_05),
            listOf(R.raw.az_clock_m_06),
            listOf(R.raw.az_clock_m_07),
            listOf(R.raw.az_clock_m_08),
            listOf(R.raw.az_clock_m_09),
            listOf(R.raw.az_clock_m_10),
            listOf(R.raw.az_clock_m_11),
            listOf(R.raw.az_clock_m_12),
            listOf(R.raw.az_clock_m_13),
            listOf(R.raw.az_clock_m_14),
            listOf(R.raw.az_clock_m_15),
            listOf(R.raw.az_clock_m_16),
            listOf(R.raw.az_clock_m_17),
            listOf(R.raw.az_clock_m_18),
            listOf(R.raw.az_clock_m_19),
            listOf(R.raw.az_clock_m_20),
            listOf(R.raw.az_clock_m_21),
            listOf(R.raw.az_clock_m_22),
            listOf(R.raw.az_clock_m_23),
            listOf(R.raw.az_clock_m_24),
            listOf(R.raw.az_clock_m_25),
            listOf(R.raw.az_clock_m_26),
            listOf(R.raw.az_clock_m_27),
            listOf(R.raw.az_clock_m_28),
            listOf(R.raw.az_clock_m_29),
            listOf(R.raw.az_clock_m_30),
            listOf(R.raw.az_clock_m_31),
            listOf(R.raw.az_clock_m_32),
            listOf(R.raw.az_clock_m_33),
            listOf(R.raw.az_clock_m_34),
            listOf(R.raw.az_clock_m_35),
            listOf(R.raw.az_clock_m_36),
            listOf(R.raw.az_clock_m_37),
            listOf(R.raw.az_clock_m_38),
            listOf(R.raw.az_clock_m_39),
            listOf(R.raw.az_clock_m_40),
            listOf(R.raw.az_clock_m_41),
            listOf(R.raw.az_clock_m_42),
            listOf(R.raw.az_clock_m_43),
            listOf(R.raw.az_clock_m_44),
            listOf(R.raw.az_clock_m_45),
            listOf(R.raw.az_clock_m_46),
            listOf(R.raw.az_clock_m_47),
            listOf(R.raw.az_clock_m_48),
            listOf(R.raw.az_clock_m_49),
            listOf(R.raw.az_clock_m_50),
            listOf(R.raw.az_clock_m_51),
            listOf(R.raw.az_clock_m_52),
            listOf(R.raw.az_clock_m_53),
            listOf(R.raw.az_clock_m_54),
            listOf(R.raw.az_clock_m_55),
            listOf(R.raw.az_clock_m_56),
            listOf(R.raw.az_clock_m_57),
            listOf(R.raw.az_clock_m_58),
            listOf(R.raw.az_clock_m_59)
        ),
        R.raw.az_clock_end
    ),

}