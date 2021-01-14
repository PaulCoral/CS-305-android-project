package com.github.lepaincestbon.bootcamp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.lepaincestbon.bootcamp.weatherforecast.weatherservice.WeatherForecastReport

class DisplayWeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_weather)
        val report = intent.getSerializableExtra(WEATHER_REPORT_MESSAGE) as WeatherForecastReport
        setViewFromReport(report)
    }

    fun setViewFromReport(report: WeatherForecastReport) {
        val descriptionView = findViewById<TextView>(R.id.weatherDescription)
        val iconView = findViewById<ImageView>(R.id.weatherIcon)

        descriptionView.apply {
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