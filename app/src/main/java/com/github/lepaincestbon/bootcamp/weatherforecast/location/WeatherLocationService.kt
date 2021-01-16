package com.github.lepaincestbon.bootcamp.weatherforecast.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import java.util.function.Consumer

class WeatherLocationService(private val locationManager: LocationManager) : LocationService {
    constructor(context: Context) : this(fromContext(context))

    private val listeners: MutableMap<Int, LocationListener> = HashMap()
    private var globalId = 0

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Location? {
        val provider = locationManager.getBestProvider(Criteria(), true) ?: return null
        try {
            // return null if no location given
            val loc = locationManager.getLastKnownLocation(provider) ?: return null
            return loc.run {
                Location(latitude, longitude)
            }
        } catch (ex: IllegalArgumentException) {
            return null
        } catch (ex: SecurityException) {
            throw ex
        }
    }

    @SuppressLint("MissingPermission")
    override fun subscribeToLocationUpdates(consumer: Consumer<Location>):Int {
        val listener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location?) {
                location?.let {
                    consumer.accept(Location(it.latitude, it.longitude))
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        val thisId = globalId
        listeners.apply {
            put(thisId, listener)
            globalId += 1
        }


        try {
            locationManager.getBestProvider(Criteria(), true)?.let { provider ->
                getCurrentLocation()?.let {
                    consumer.accept(it)
                    locationManager.requestLocationUpdates(provider, minTime, minDistance, listener)
                }
            }
        } catch (e: SecurityException) {
            throw e
        } finally {
            return thisId
        }
    }

    override fun unsubscribeToLocationUpdates(id: Int) {
        listeners.remove(id)?.let {
            locationManager.removeUpdates(it)
        }
    }

    companion object {
        private const val minTime = 1000L
        private const val minDistance = 1f
        fun fromContext(context: Context) =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }
}