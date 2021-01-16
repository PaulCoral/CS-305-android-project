package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.EmptyForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService
import kotlinx.android.synthetic.main.activity_weather_fore_cast.*


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

        cityNameField = findViewById(R.id.cityName)
        gpsSwitch = findViewById(R.id.gps)
        weatherTextView = findViewById(R.id.textViewWeather)
        fetchWeatherButton = findViewById(R.id.fetchWeather)
        fetchWeatherButton.setOnClickListener { displayWeather() }
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
            textViewWeather.text = resources.getString(R.string.weather_location_error)
            return
        }

        val weatherReport =
            WeatherForecastService(resources.getString(R.string.openweather_api_key))
                .requestWeather(location)
        when (weatherReport) {
            is EmptyForecastReport -> weatherTextView.text =
                resources.getString(R.string.weather_display_error)
            is WeatherForecastReport -> {
                weatherTextView.text = getString(R.string.weather_textview_loading)
                displayWeatherActivity(weatherReport)
            }

        }
    }


    private fun displayWeatherActivity(report: WeatherForecastReport) {
        val iconView = findViewById<ImageView>(R.id.weatherIconImageView)

        weatherTextView.apply {
            val textReport =
                """${report.main} : ${report.description}
                    |It is ${report.temp} outside.
                """.trimMargin()
            text = textReport
        }

        report.icon.run {
            if (this.isEmpty()) return
            iconView.setImageBitmap(BitmapFactory.decodeByteArray(this, 0, this.size))
        }
    }

}