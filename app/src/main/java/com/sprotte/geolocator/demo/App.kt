package com.sprotte.geolocator.demo

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}