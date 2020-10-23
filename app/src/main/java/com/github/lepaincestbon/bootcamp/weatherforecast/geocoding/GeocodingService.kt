package com.github.lepaincestbon.bootcamp.weatherforecast.geocoding

import android.location.Address
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location

interface GeocodingService {
    fun getLocationFromName(name: String) : Location
    fun getAddressFromLocation(location: Location) : Address
}