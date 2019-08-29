package com.yumetsuki.yuzusoftappwidget

import android.app.Application
import android.content.ContextWrapper

private lateinit var INSTANCE: Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

}

object AppContext: ContextWrapper(INSTANCE)