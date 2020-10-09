package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.github.lepaincestbon.bootcamp.weatherforecast.GeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.LocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.WeatherService

class WeatherForeCast : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_fore_cast)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 0
        )
        val b1 = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val b2 = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        println("Booleans permission: $b1 and $b2")
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
                LocationService(appContext).getCurrentLocation()
            } else {
                GeocodingService(appContext).getLocationFromName(locName)
            }
        println("The loc is $loc")
        val weatherAsText = loc?.run {
            val jobj = WeatherService(resources.getString(R.string.openweather_api_key)).requestWeather(loc)
            if (jobj != null) {
                val temp = WeatherService.getTemperatureFromJson(jobj)
                val description = WeatherService.getDescriptionFromJson(jobj)

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