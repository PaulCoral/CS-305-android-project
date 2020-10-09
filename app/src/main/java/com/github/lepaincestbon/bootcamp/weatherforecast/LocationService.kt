package com.github.lepaincestbon.bootcamp.weatherforecast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class LocationService(private val context: Context) {
    private val locService =
        context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    fun getCurrentLocation(): Location? {
        val lastKnownLoc = locService?.run {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println(">>>>> else")
                null
            } else {
                println(">>>> if")
                val criteria = Criteria()
                val provider = getBestProvider(criteria, false)
                println("provider is >>>> $provider")
                getLastKnownLocation(provider)


            }
        }

        return lastKnownLoc?.run {
            Location(latitude, longitude)
        }
    }
}