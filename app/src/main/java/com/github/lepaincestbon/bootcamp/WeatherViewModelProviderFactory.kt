package com.github.lepaincestbon.bootcamp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService

class WeatherViewModelProviderFactory(private val context: Context, private val appId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (modelClass).getConstructor(WeatherActivityViewModel::class.java).newInstance(
            WeatherLocationService(context),
            WeatherGeocodingService(context),
            WeatherForecastService(appId)
        )
    }

}