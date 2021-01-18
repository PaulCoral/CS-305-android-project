package com.github.lepaincestbon.bootcamp.weatherforecast.geocoding

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class WeatherGeocodingService private constructor(private val geocoder: Geocoder) :
    GeocodingService {
    companion object {
        const val MAX_LOCATION_RESULT = 5
    }

    @Inject
    constructor(context: Context) : this(Geocoder(context))

    override fun getLocationFromName(name: String): CompletableFuture<Location?> =
        CompletableFuture.supplyAsync {
            //try {
            val addressList = geocoder.getFromLocationName(name, MAX_LOCATION_RESULT)
            val addr = addressList
                .stream()
                .filter { it.hasLatitude() && it.hasLongitude() }
                .map { Location(it.latitude, it.longitude) }
                .findFirst()
                .get()
            Log.e("addr", addr.toString())
            addr
            /*} catch (ex: IOException) {
                null
            } catch (ex: NoSuchElementException) {
                null
            }*/
        }


    override fun getAddressFromLocation(location: Location): CompletableFuture<Address?> =
        CompletableFuture.supplyAsync {
            try {
                location.run { geocoder.getFromLocation(latitude, longitude, 1) }.first()
            } catch (ex: NoSuchElementException) {
                null
            }
        }


}