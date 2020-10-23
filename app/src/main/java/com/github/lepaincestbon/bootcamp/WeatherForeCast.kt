package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
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

        cityNameField = findViewById<EditText>(R.id.cityName)
        gpsSwitch = findViewById<Switch>(R.id.gps)
        weatherTextView = findViewById<TextView>(R.id.textViewWeather)
        fetchWeatherButton = findViewById<Button>(R.id.fetchWeather)
        fetchWeatherButton.setOnClickListener {
            displayWeather()
        }
    }

    fun displayWeather() {

        val location =
            when (gpsSwitch.isChecked) {
                true -> WeatherLocationService(this).getCurrentLocation()
                false -> {
                    val locationName = cityNameField.text.toString()
                    WeatherGeocodingService(this).getLocationFromName(locationName)
                }
            } ?: return

        val weatherReport =
            WeatherForecastService(resources.getString(R.string.openweather_api_key))
                .requestWeather(location)
        when (weatherReport) {
            is EmptyForecastReport -> textViewWeather.text =
                resources.getString(R.string.weather_display_error)
            is WeatherForecastReport -> textViewWeather.text = weatherReport.toString()
        }
    }


    /*@SuppressLint("UseSwitchCompatOrMaterialCode")
    fun displayWeather(view: View) {
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
            val jobj =
                WeatherForecastService(resources.getString(R.string.openweather_api_key)).requestWeather(
                    loc
                )
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
    }*/
}