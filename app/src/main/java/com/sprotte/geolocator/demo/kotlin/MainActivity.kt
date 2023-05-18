package com.sprotte.geolocator.demo.kotlin

import android.Manifest.permission
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.sprotte.geolocator.demo.R
import com.sprotte.geolocator.demo.misc.getDynamicLinks
import com.sprotte.geolocator.geofencer.Geofencer
import com.sprotte.geolocator.geofencer.models.Geofence
import com.sprotte.geolocator.tracking.LocationTracker
import com.sprotte.geolocator.utils.showTwoButtonDialog
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDynamicLinks()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Timber.d(" onNewIntent: action :: $action")
        Timber.d("onNewIntent: data :: $data")
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
