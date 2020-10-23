package com.github.lepaincestbon.bootcamp.weatherforecast.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import java.lang.IllegalArgumentException

class WeatherLocationService(private val locationManager: LocationManager) : LocationService {
    companion object{
        fun fromContext(context: Context) =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }
    constructor(context: Context) : this(fromContext(context))

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Location? {
        val provider = locationManager.getBestProvider(Criteria(),true) ?: return null
        try {
            // return null if no location given
            val loc = locationManager.getLastKnownLocation(provider) ?: return null
            return loc.run {
                Location(latitude,longitude)
            }
        } catch (ex : IllegalArgumentException){
            return null
        } catch (ex : SecurityException){
            throw ex
        }
    }
}