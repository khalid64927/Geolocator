package com.sprotte.geolocator.demo

import android.app.Application
import timber.log.Timber
import com.appsflyer.AppsFlyerLib

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        val appsFlyerLib = AppsFlyerLib.getInstance()
        appsFlyerLib.run {
            init("6VTPGqbLbdMPSBkiiTzEMJ", null, applicationContext)
            start(applicationContext)
            setDebugLog(true)
        }

    }
}