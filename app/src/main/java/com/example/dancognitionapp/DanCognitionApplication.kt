package com.example.dancognitionapp

import android.app.Application
import timber.log.Timber

class DanCognitionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}