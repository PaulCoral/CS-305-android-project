package com.github.lepaincestbon.bootcamp.weatherforecast

import android.content.Context
import android.location.Geocoder

class GeocodingService(context: Context) {
    private val geocoder = Geocoder(context)

    fun getLocationFromName(name: String): Location {
        val addressList = geocoder.getFromLocationName(name, 1)
        return if (addressList.size < 1) {
            Location()
        } else {
            val address = addressList[0]
            address.run {
                Location(latitude, longitude)
            }
        }

    }
}