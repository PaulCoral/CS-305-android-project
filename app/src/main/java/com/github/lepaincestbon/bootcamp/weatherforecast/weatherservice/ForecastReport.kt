package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

sealed class ForecastReport

data class WeatherForecastReport(
    val temp: Int,
    val description: String
) : ForecastReport() {
    override fun toString(): String =
        """Weather : $description
            |Temperature : $temp
        """.trimMargin()

}

object EmptyForecastReport : ForecastReport()
