package com.github.lepaincestbon.bootcamp

import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingServiceModule
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationServiceModule
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastServiceModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.rules.RuleChain

@HiltAndroidTest
@UninstallModules(
    WeatherForecastServiceModule::class,
    WeatherLocationServiceModule::class,
    WeatherGeocodingServiceModule::class
)
class WeatherForeCastActivityTest {
    @get:Rule
    val rule: RuleChain = RuleChain.outerRule(HiltAndroidRule(this))
}