package com.github.lepaincestbon.bootcamp.weatherforecast.geocoding

import android.content.Context
import android.location.Geocoder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
abstract class WeatherGeocodingServiceModule {
    @Binds
    abstract fun bindWeatherGeocodingService(weatherGeocodingServiceImpl: WeatherGeocodingService): GeocodingService

    companion object {
        @Provides
        fun providesGeocoder(@ApplicationContext context: Context) = Geocoder(context)
    }
}