package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
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

        private fun getTemperatureFromJson(jobj: JSONObject) =
            getCurrentObj(jobj).getInt("temp")

        private fun getFromWeatherFromJson(jobj: JSONObject, elem: String): String {
            val errorMsg = "Sorry, no weather information available"
            val array = getCurrentObj(jobj).getJSONArray("weather")

            return if (array.length() == 0) {
                errorMsg
            } else {
                try {
                    array.getJSONObject(0).getString(elem)
                } catch (e: JSONException) {
                    "$errorMsg : $e"
                }

            }

        }

        private fun getDescriptionFromJson(jobj: JSONObject): String =
            getFromWeatherFromJson(jobj, "description")


        private fun getMainFromJson(jobj: JSONObject): String =
            getFromWeatherFromJson(jobj, "main")

        private fun getIconIdFromJson(jobj: JSONObject): String =
            getFromWeatherFromJson(jobj, "icon")


        fun getIconFromId(imgId: String): Bitmap? {
            val url = URL("https://openweathermap.org/img/wn/${imgId}@2x.png")

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
                    val instream = inputStream

                    val bitmap = BitmapFactory.decodeStream(instream)


                    inputStream?.close()
                    disconnect()
                    bitmap
                }
                connectionResponse
            } finally {
                connection?.inputStream?.close()
                connection?.disconnect()
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
                getMainFromJson(this),
                getTemperatureFromJson(this),
                getDescriptionFromJson(this),
                getIconIdFromJson(this).run {
                    val icon = getIconFromId(this)
                    val byteout = ByteArrayOutputStream()
                    icon?.compress(Bitmap.CompressFormat.PNG, 100, byteout)
                    byteout.toByteArray()
                }
            )
        }
    }
}