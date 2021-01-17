package com.github.lepaincestbon.bootcamp

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.GeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import com.github.lepaincestbon.bootcamp.weatherforecast.location.LocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.EmptyForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.ForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.ForecastService
import java.io.IOException


class WeatherActivityViewModel @ViewModelInject constructor(
    private val locationService: LocationService,
    private val geocodingService: GeocodingService,
    private val forecastService: ForecastService
) : ViewModel() {
    val currentWeather: MutableLiveData<ForecastReport> = MutableLiveData(null)
    var canQueryWeather: LiveData<Boolean>
        private set
    val isUsingGPS: MutableLiveData<Boolean> = MutableLiveData(false)

    var currentLocation: MutableLiveData<Location> = MutableLiveData(null)
    val selectedAddress: MutableLiveData<String> = MutableLiveData(null)
    var locationAtSelectedAddress: LiveData<Location?>
    var selectedLocation: LiveData<Location?>

    init {
        locationService.subscribeToLocationUpdates { loc -> currentLocation.postValue(loc) }
        locationAtSelectedAddress = Transformations.map(selectedAddress) {
            Log.e("locationMap", it ?: "<null>")
            it?.let {
                try {
                    geocodingService.getLocationFromName(it)
                } catch (e: IOException) {
                    null
                }
            }
        }

        selectedLocation = Transformations.switchMap(isUsingGPS) {
            if (it == true) currentLocation else locationAtSelectedAddress
        }

        canQueryWeather = Transformations.map(selectedLocation) { it != null }
    }

    fun setIsUsingGPS(isUsing: Boolean) = isUsingGPS.postValue(isUsing)
    fun setSelectedAddress(address: String) = selectedAddress.postValue(address)
    fun refreshWeather() {
        val loc = selectedLocation.value
        if (loc == null) {
            Log.e("refreshWeather", "loc == null")

            currentWeather.postValue(EmptyForecastReport)
        } else {
            Log.e("refreshWeather", "loc != null")

            try {
                currentWeather.postValue(forecastService.requestWeather(loc))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}