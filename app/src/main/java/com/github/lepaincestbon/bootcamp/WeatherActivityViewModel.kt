package com.github.lepaincestbon.bootcamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.GeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import com.github.lepaincestbon.bootcamp.weatherforecast.location.LocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.ForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.ForecastService
import java.io.IOException

class WeatherActivityViewModel(
    locationService: LocationService,
    geocodingService: GeocodingService,
    private val forecastService: ForecastService
) : ViewModel() {
    val currentWeather: MutableLiveData<ForecastReport> = MutableLiveData(null)
    var canQueryWeather: LiveData<Boolean>
        private set
    val isUsingGPS: MutableLiveData<Boolean> = MutableLiveData(null)

    private val currentLocation: MutableLiveData<Location> = MutableLiveData(null)
    private val selectedAddress: MutableLiveData<String> = MutableLiveData(null)
    private var locationAtSelectedAddress: LiveData<Location?> = MutableLiveData(null)
    private var selectedLocation: LiveData<Location?> = MutableLiveData(null)

    init {
        locationService.subscribeToLocationUpdates { loc -> currentLocation.postValue(loc) }
        locationAtSelectedAddress = Transformations.map(selectedAddress) {
            it?.let {
                geocodingService.getLocationFromName(it)
            }
        }

        selectedLocation = Transformations.switchMap(isUsingGPS) {
            if (it) currentLocation else locationAtSelectedAddress
        }

        canQueryWeather = Transformations.map(selectedLocation) { it != null }
    }

    fun setIsUsingGPS(isUsing: Boolean) = isUsingGPS.postValue(isUsing)
    fun setSelectedAddress(address: String) = selectedAddress.postValue(address)
    fun refreshWeather() {
        val loc = selectedLocation.value
        if (loc == null) {
            currentWeather.postValue(null)
        } else {
            try {
                currentWeather.postValue(forecastService.requestWeather(loc))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}