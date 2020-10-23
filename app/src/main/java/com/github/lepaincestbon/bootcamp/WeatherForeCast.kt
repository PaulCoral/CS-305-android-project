package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService


class WeatherForeCast : AppCompatActivity() {
    companion object{
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_fore_cast)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun displayWeather(view: View) {
        val errorMsg = "Sorry, something went wrong"

        val editText = findViewById<EditText>(R.id.cityName)
        val locName = editText.text.toString()
        println("locName : $locName")

        val gps = findViewById<Switch>(R.id.gps)
        val isGpsEnabled = gps.isChecked
        println("gps enabled : $isGpsEnabled")

        val weatherDisplay = findViewById<TextView>(R.id.textViewWeather)

        val appContext = this

        val loc =
            if (isGpsEnabled) {
                WeatherLocationService(appContext).getCurrentLocation()
            } else {
                WeatherGeocodingService(appContext).getLocationFromName(locName)
            }
        println("The loc is $loc")
        val weatherAsText = loc?.run {
            val jobj = WeatherForecastService(resources.getString(R.string.openweather_api_key)).requestWeather(loc)
            if (jobj != null) {
                val temp = WeatherForecastService.getTemperatureFromJson(jobj)
                val description = WeatherForecastService.getDescriptionFromJson(jobj)

                """hi, here's the weather for today :
                    | - Temperature : $temp
                    | - Description : $description
                    | Thanks !
                """.trimMargin()
            } else {
                // if null json object, something went wrong
                errorMsg
            }
        }

        weatherDisplay.apply {
            text = weatherAsText
        }
    }
}