package com.github.lepaincestbon.bootcamp

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


const val EXTRA_MESSAGE = "com.github.lepaincestbon.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // this allow using networking on the main thread (not allowed by default on Android)
        // TODO: Remove this when networking is async
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitNetwork().build())

    }

    fun sendMessage(view: View) {
        val textView = findViewById<EditText>(R.id.mainName)
        val name = textView.text.toString()

        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, name)
        }
        startActivity(intent)
    }

    fun goToWeather(view: View) {
        val intent = Intent(this, WeatherForeCast::class.java)
        startActivity(intent)
    }
}