package com.github.lepaincestbon.bootcamp

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.lepaincestbon.bootcamp.weatherforecast.geocoding.WeatherGeocodingService
import com.github.lepaincestbon.bootcamp.weatherforecast.location.WeatherLocationService
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.EmptyForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.ForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastReport
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastService


class WeatherForeCast : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var viewModel: WeatherActivityViewModel
    private lateinit var cityNameField: EditText

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var gpsSwitch: Switch
    private lateinit var fetchWeatherButton: Button

    private lateinit var weatherTextView: TextView
    private lateinit var iconView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_fore_cast)

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )

        /* Create view model */
        //viewModel = ViewModelProvider(this).get(WeatherActivityViewModel::class.java)
        viewModel = WeatherActivityViewModel(
            WeatherLocationService(this),
            WeatherGeocodingService(this),
            WeatherForecastService(resources.getString(R.string.openweather_api_key))
        )
        /*viewModel = ViewModelProvider(
            this,
            WeatherViewModelProviderFactory(this, resources.getString(R.string.openweather_api_key))
        ).get(WeatherActivityViewModel::class.java)*/

        /* Set observers */
        viewModel.currentWeather.observe(this, this::displayWeatherActivity)
        viewModel.isUsingGPS.observe(this) { cityNameField.isEnabled = !it }
        viewModel.canQueryWeather.observe(this) { fetchWeatherButton.isEnabled = it }


        /* Set listeners */

        cityNameField = findViewById(R.id.cityName)
        cityNameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.run {
                    viewModel.setSelectedAddress(toString())
                }
            }
        })

        gpsSwitch = findViewById(R.id.gps)
        gpsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsUsingGPS(isChecked)
        }

        fetchWeatherButton = findViewById(R.id.fetchWeather)
        fetchWeatherButton.setOnClickListener {
            viewModel.refreshWeather()
        }

        weatherTextView = findViewById(R.id.textViewWeather)
        iconView = findViewById(R.id.weatherIconImageView)
    }


    private fun displayWeatherActivity(report: ForecastReport?) {
        report ?: return
        when (report) {
            is EmptyForecastReport -> weatherTextView.text =
                resources.getString(R.string.weather_display_error)
            is WeatherForecastReport -> {
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
    }

}