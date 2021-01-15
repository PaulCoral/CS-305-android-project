package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import android.content.Context
import com.github.lepaincestbon.bootcamp.R
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ApplicationComponent::class)
abstract class WeatherForecastServiceModule {
    @Binds
    abstract fun bindWeatherForecastService(impl: WeatherForecastService): ForecastService

    @Provides
    fun providesAppid(@ApplicationContext context: Context) =
        context.getString(R.string.openweather_api_key)
}