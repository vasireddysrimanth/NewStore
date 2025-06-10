package com.dev.storeapp

import android.app.Application
import com.dev.storeapp.app.utils.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StoreApp  :Application(){
    override fun onCreate() {
        super.onCreate()
        AppLogger.d("StoreApp", "Application started")
    }
}