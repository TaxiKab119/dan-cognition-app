package com.example.dancognitionapp

import android.app.Application
import com.example.dancognitionapp.di.AppContainer
import com.example.dancognitionapp.di.AppDataContainer
import timber.log.Timber

class DanCognitionApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        Timber.plant(Timber.DebugTree())
    }
}