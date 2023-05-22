package com.sprotte.geolocator.demo

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.deeplink.DeepLink
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.sprotte.geolocator.demo.misc.toastL
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.Objects

class App : Application() {

    var deferred_deep_link_processed_flag = false
    var conversionData: Map<String, Any>? = null

    private val cl = object: AppsFlyerConversionListener {
        override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
            val sb = StringBuilder()
            p0?.forEach {
                sb.append("${it.key} :: ${it.value}")
            }
            Timber.d("onConversionDataSuccess :: $sb")
            toastL("onConversionDataSuccess")
            try {
                conversionSuccess(p0)
            }catch (e:Exception){
                Timber.e(e)
                e.printStackTrace()
            }
        }

        override fun onConversionDataFail(p0: String?) {
            Timber.d("onConversionDataFail :: $p0")
            toastL("onConversionDataFail")
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            val sb = StringBuilder()
            p0?.forEach {
                sb.append("${it.key} :: ${it.value}")
            }
            Timber.d("onAppOpenAttribution :: $sb")
            toastL("onAppOpenAttribution")
        }

        override fun onAttributionFailure(p0: String?) {
            Timber.d("onAttributionFailure :: $p0")
            toastL("onAttributionFailure")
        }

    }

    private val deepLinkResult = object: DeepLinkListener {
        override fun onDeepLinking(it: DeepLinkResult) {
            Timber.d("DeepLinkResult status is ${it.status.name}")
            if(it.status == DeepLinkResult.Status.ERROR || it.status == DeepLinkResult.Status.NOT_FOUND){
                toastL(" DeepLinkResult.Status.NOT_FOUND  or DeepLinkResult.Status.ERROR")
                return
            }
            toastL(" DeepLinkResult.Status.FOUND ")

            try{
                it.deepLink
                toastL("is deferred deeplink : ${it.deepLink.isDeferred}")
                toastL("deeplink data :${it.deepLink}")
            }catch (e: Exception){
                Timber.e("exception ${e.message}")
                e.printStackTrace()
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        val tree = if(BuildConfig.DEBUG) Timber.DebugTree() else CrashTree()
        Timber.plant(tree)
        val appsFlyerLib = AppsFlyerLib.getInstance()

        appsFlyerLib.run {
            setDebugLog(true)
            setMinTimeBetweenSessions(0)
            setAppInviteOneLink("")
            subscribeForDeepLink(deepLinkResult)
            init("6VTPGqbLbdMPSBkiiTzEMJ", cl, applicationContext)
            start(applicationContext)
        }

    }


    private fun conversionSuccess(conversionDataMap: MutableMap<String, Any>?){
        if(conversionDataMap.isNullOrEmpty()){
            return
        }
        for (attrName in conversionDataMap.keys)
            Timber.d(
            "Conversion attribute: " + attrName + " = " + conversionDataMap[attrName]
        )
        val status = Objects.requireNonNull(
            conversionDataMap["af_status"]
        ).toString()
        if (status == "Non-organic") {
            if (Objects.requireNonNull<Any?>(conversionDataMap["is_first_launch"])
                    .toString() == "true"
            ) {
                Timber.d(
                    "Conversion: First Launch"
                )
                //Deferred deep link in case of a legacy link
                if (deferred_deep_link_processed_flag == true) {
                    Timber.d(
                        "Deferred deep link was already processed by UDL. The DDL processing in GCD can be skipped."
                    )
                    deferred_deep_link_processed_flag = false
                } else {
                    deferred_deep_link_processed_flag = true
                    if (conversionDataMap.containsKey("fruit_name")) {
                        conversionDataMap["deep_link_value"] = conversionDataMap!!["fruit_name"]!!
                    }
                    val fruitNameStr = conversionDataMap["deep_link_value"] as String?
                    val deepLinkData: DeepLink? = mapToDeepLinkObject(conversionDataMap)
                    //goToFruit(fruitNameStr, deepLinkData)
                }
            } else {
                Timber.d(
                    "Conversion: Not First Launch"
                )
            }
        } else {
            Timber.d(
                "Conversion: This is an organic install."
            )
        }
        conversionData = conversionDataMap

    }

    private fun mapToDeepLinkObject(conversionDataMap: MutableMap<String, Any>): DeepLink? {
        try {
            val objToStr = Gson().toJson(conversionDataMap)
            return DeepLink.AFInAppEventType(JSONObject(objToStr))
        } catch (e: JSONException) {
            Timber.d(
                "Error when converting map to DeepLink object: $e"
            )
        }
        return null
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