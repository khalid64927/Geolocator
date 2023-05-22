package com.sprotte.geolocator.demo.kotlin

import android.Manifest.permission
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.sprotte.geolocator.demo.R
import com.sprotte.geolocator.demo.misc.getDynamicLinks
import com.sprotte.geolocator.demo.misc.toastL
import com.sprotte.geolocator.geofencer.Geofencer
import com.sprotte.geolocator.geofencer.models.Geofence
import com.sprotte.geolocator.tracking.LocationTracker
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var referrerClient: InstallReferrerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        referrerClient = InstallReferrerClient.newBuilder(this).build()






        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        Timber.d("InstallReferrerResponse.OK")

                        toastL("InstallReferrerResponse.OK")
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        Timber.d("InstallReferrerResponse.FEATURE_NOT_SUPPORTED")
                        toastL("InstallReferrerResponse.FEATURE_NOT_SUPPORTED")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Timber.d("InstallReferrerResponse.SERVICE_UNAVAILABLE")
                        toastL("InstallReferrerResponse.SERVICE_UNAVAILABLE")
                    }
                }

                val response: ReferrerDetails = referrerClient.installReferrer
                referrerClient.installReferrer
                val referrerUrl: String = response.installReferrer
                val referrerClickTime: Long = response.referrerClickTimestampSeconds
                val appInstallTime: Long = response.installBeginTimestampSeconds
                val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
                val message = " response : $response \n " +
                        " referrerUrl : $referrerUrl \n " +
                        " referrerClickTime : $referrerClickTime \n " +
                        " appInstallTime : $appInstallTime \n " +
                        " instantExperienceLaunched : $instantExperienceLaunched"

                getDynamicLinks(message)
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                toastL("onInstallReferrerServiceDisconnected")
            }
        })


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Timber.d(" onNewIntent: action :: $action")
        Timber.d("onNewIntent: data :: $data")
        getDynamicLinks("onNewIntent ::")
    }

    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    private fun registerGeofenceUpdates() {
        val geofence = Geofence(
            id = UUID.randomUUID().toString(),
            latitude = 51.0899232,
            longitude = 5.968358,
            radius = 30.0,
            title = "Germany",
            message = "Entered Germany",
            transitionType = com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
        )

        Geofencer(this).addGeofence(geofence, GeofenceIntentService::class.java) { /* successfully added geofence */ }
    }

    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    private fun registerLocationUpdateEvents() {
        LocationTracker.requestLocationUpdates(this, LocationTrackerService::class.java)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.navHost).navigateUp() || super.onSupportNavigateUp()
}
