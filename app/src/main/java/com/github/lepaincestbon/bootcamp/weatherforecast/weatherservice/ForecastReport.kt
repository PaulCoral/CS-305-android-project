package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

sealed class ForecastReport

data class WeatherForecastReport(
    val temp: Int,
    val description: String
) : ForecastReport()

object EmptyForecastReport : ForecastReport()
