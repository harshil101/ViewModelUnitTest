package com.lab49.taptosnap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuizApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}