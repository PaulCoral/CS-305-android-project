package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location

interface ForecastService {
    fun requestWeather(loc: Location): ForecastReport
}