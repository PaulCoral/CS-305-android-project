package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import android.graphics.Bitmap
import java.io.Serializable

sealed class ForecastReport : Serializable

@Suppress("ArrayInDataClass")
data class WeatherForecastReport(
    val main: String,
    val temp: Int,
    val description: String,
    val icon: Bitmap?
) : ForecastReport()

object EmptyForecastReport : ForecastReport()
