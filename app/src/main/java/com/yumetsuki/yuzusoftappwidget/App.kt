package com.yumetsuki.yuzusoftappwidget

import android.app.Application
import android.content.ContextWrapper
import androidx.work.Configuration

private lateinit var INSTANCE: Application

class App: Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

}

object AppContext: ContextWrapper(INSTANCE)