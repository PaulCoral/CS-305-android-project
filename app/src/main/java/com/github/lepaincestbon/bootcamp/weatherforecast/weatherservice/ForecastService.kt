package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import java.util.concurrent.CompletableFuture

interface ForecastService {
    fun requestWeather(loc: Location): CompletableFuture<ForecastReport>
}