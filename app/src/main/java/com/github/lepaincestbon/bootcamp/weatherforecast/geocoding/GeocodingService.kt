package com.github.lepaincestbon.bootcamp.weatherforecast.geocoding

import android.location.Address
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import java.util.concurrent.CompletableFuture

interface GeocodingService {
    fun getLocationFromName(name: String): CompletableFuture<Location?>
    fun getAddressFromLocation(location: Location): CompletableFuture<Address?>
}