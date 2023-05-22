package com.sprotte.geolocator.demo

import android.app.Application
import timber.log.Timber
import com.appsflyer.AppsFlyerLib

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppsFlyerLib.getInstance().init("6VTPGqbLbdMPSBkiiTzEMJ", null, this)
    }
}