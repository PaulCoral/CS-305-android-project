package com.github.lepaincestbon.bootcamp.weatherforecast.location

import android.content.Context
import android.location.LocationManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
abstract class WeatherLocationServiceModule {

    @Binds
    abstract fun bindWeatherLocationService(impl: WeatherLocationService): WeatherLocationService

    companion object {
        @Provides
        fun providesLocationManager(@ApplicationContext context: Context): LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}