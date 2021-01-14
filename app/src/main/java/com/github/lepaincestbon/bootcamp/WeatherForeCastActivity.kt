package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.EmptyForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather_fore_cast.*


const val WEATHER_REPORT_MESSAGE = "com.github.lepaincestbon.WEATHER_REPORT_MESSAGE"

@AndroidEntryPoint
class WeatherForeCast : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var cityNameField: EditText

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var gpsSwitch: Switch
    private lateinit var weatherTextView: TextView
    private lateinit var fetchWeatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_fore_cast)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )

        cityNameField = findViewById<EditText>(R.id.cityName)
        gpsSwitch = findViewById<Switch>(R.id.gps)
        weatherTextView = findViewById<TextView>(R.id.textViewWeather)
        fetchWeatherButton = findViewById<Button>(R.id.fetchWeather)
        fetchWeatherButton.setOnClickListener {
            displayWeather()
        }
    }

    private fun displayWeather() {

        val location =
            if (gpsSwitch.isChecked) {
                WeatherLocationService(this).getCurrentLocation()
            } else {
                val locationName = cityNameField.text.toString()
                WeatherGeocodingService(this).getLocationFromName(locationName)
            }

        if (location == null) {
            textViewWeather.text = "Could not get your location..."
            return
        }

        val weatherReport =
            WeatherForecastService(resources.getString(R.string.openweather_api_key))
                .requestWeather(location)
        when (weatherReport) {
            is EmptyForecastReport -> textViewWeather.text =
                resources.getString(R.string.weather_display_error)
            is WeatherForecastReport -> {
                textViewWeather.text = "Loading ..."
                displayWeatherActivity(weatherReport)
            }

        }
    }


    private fun displayWeatherActivity(report: WeatherForecastReport) {
        val intent = Intent(this, DisplayWeatherActivity::class.java).apply {
            putExtra(WEATHER_REPORT_MESSAGE, report)

        }
        startActivity(intent)
    }

}