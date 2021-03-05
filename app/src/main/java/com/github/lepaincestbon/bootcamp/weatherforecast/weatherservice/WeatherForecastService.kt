package com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice

import android.graphics.Bitmap
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.github.lepaincestbon.bootcamp.weatherforecast.location.Location
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class WeatherForecastService @Inject constructor(
    private val appID: String,
    private val requestQueue: RequestQueue
) : ForecastService {
    companion object {

        @Suppress("unused")
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

    }

    override fun requestWeather(loc: Location): CompletableFuture<ForecastReport> {
        val queryUrl = loc.run {
            "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&appid=${appID}&units=${UNITS.CELSIUS}"
        }

        val completableFuture = CompletableFuture<JSONObject>()
        val cfForecast = CompletableFuture<Pair<JSONObject, Bitmap>>()

        val jsonHttpRequest =
            JsonObjectRequest(Request.Method.GET, queryUrl, null, {
                completableFuture.complete(it)
            }, {
                completableFuture.completeExceptionally(it)
            })

        requestQueue.add(jsonHttpRequest)

        completableFuture
            .thenApply {
                it.run {
                    val iconId = getIconIdFromJson(this)
                    val url = "https://openweathermap.org/img/wn/${iconId}@4x.png"
                    val imageRequest = ImageRequest(url, { bitmap ->
                        cfForecast.complete(Pair(this, bitmap))
                    },
                        0,
                        0,
                        null,
                        null, { ex ->
                            cfForecast.completeExceptionally(ex)
                        })
                    requestQueue.add(imageRequest)

                }
            }
        return cfForecast.thenApply {
            val json = it.first
            val bitmap = it.second
            WeatherForecastReport(
                getMainFromJson(json),
                getTemperatureFromJson(json),
                getDescriptionFromJson(json),
                bitmap
            )
        }
    }
}