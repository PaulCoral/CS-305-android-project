package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherForecastService(private val appID: String) : ForecastService {
    companion object {

        enum class UNITS(private val unitName: String) {
            CELSIUS("metric"),
            KELVIN("standard"),
            FAHRENHEIT("imperial");

            override fun toString(): String = unitName
        }

        private fun apiResponseToJson(apiResponse: String) =
            JSONObject(JSONTokener(apiResponse))

        private fun getCurrentObj(root: JSONObject) =
            root.getJSONObject("current")

        fun getTemperatureFromJson(jobj: JSONObject) =
            getCurrentObj(jobj).getInt("temp")

        fun getDescriptionFromJson(jobj: JSONObject): String {
            val errorMsg = "Sorry, no weather information available"
            val array = getCurrentObj(jobj).getJSONArray("weather")

            return when (val l = array.length()) {
                0 -> errorMsg
                else -> array.getJSONObject(0).getString("description")
            }
        }

    }

    private fun requestWeather(lat: Double, lon: Double, unit: UNITS = UNITS.CELSIUS): String? {
        val queryUrl =
            "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lon}&appid=${appID}&units=${unit}"
        val url = URL(queryUrl)
        var connection: HttpsURLConnection? = null
        return try {
            connection = (url.openConnection() as? HttpsURLConnection)?.apply {
                readTimeout = 3000
                connectTimeout = 3000
                requestMethod = "GET"
                doInput = true
            }
            val connectionResponse = connection?.run {
                connect()

                val responseCode = responseCode
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw IOException("HTTP error code: $responseCode")
                }
                val response = inputStream?.let { stream ->
                    val inReader = InputStreamReader(stream)
                    val lines = inReader.readLines()
                    val sb = StringBuilder()
                    for (line in lines) {
                        sb.appendLine(line)
                    }
                    val urlResponse = sb.toString()
                    stream.close()

                    urlResponse
                }
                inputStream?.close()
                disconnect()
                response
            }
            connectionResponse
        } finally {
            connection?.inputStream?.close()
            connection?.disconnect()
        }


    }

    override fun requestWeather(loc: Location): ForecastReport {
        val textReport =
            loc.run { requestWeather(latitude, longitude) } ?: return EmptyForecastReport
        val jsonObj = apiResponseToJson(textReport)
        return jsonObj.run {
            WeatherForecastReport(
                getTemperatureFromJson(this),
                getDescriptionFromJson(this)
            )
        }
    }
}