package com.github.lepaincestbon.bootcamp

import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.rules.RuleChain

@HiltAndroidTest
@UninstallModules(
    WeatherForecastService::class,
    WeatherLocationService::class,
    WeatherGeocodingService::class
)
class WeatherForeCastActivityTest {
    @get:Rule
    val rule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))

}