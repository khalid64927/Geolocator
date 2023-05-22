package com.sprotte.geolocator.demo

import android.app.Application
import timber.log.Timber
import com.appsflyer.AppsFlyerLib
import com.appsflyer.deeplink.DeepLinkResult
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.sprotte.geolocator.demo.misc.toastL
import java.lang.Exception

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val tree = if(BuildConfig.DEBUG) Timber.DebugTree() else CrashTree()
        Timber.plant(tree)
        val appsFlyerLib = AppsFlyerLib.getInstance()
        appsFlyerLib.run {
            init("6VTPGqbLbdMPSBkiiTzEMJ", null, applicationContext)
            start(applicationContext)
            setDebugLog(true)
            setMinTimeBetweenSessions(0)
            subscribeForDeepLink {
                Timber.d("DeepLinkResult status is ${it.status.name}")
                if(it.status == DeepLinkResult.Status.ERROR || it.status == DeepLinkResult.Status.NOT_FOUND){
                    toastL(" DeepLinkResult.Status.NOT_FOUND  or DeepLinkResult.Status.ERROR")
                    return@subscribeForDeepLink
                }
                toastL(" DeepLinkResult.Status.FOUND ")

                try{
                    it.deepLink
                    toastL("deeplink data ${it.deepLink}")
                }catch (e: Exception){
                    Timber.e("exception ${e.message}")

                }
            }
        }

    }

}

class CrashTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Firebase.crashlytics.log(message)
    }

    override fun e(t: Throwable?) {
        t?.let { Firebase.crashlytics.recordException(it) }
    }
}