package com.github.lepaincestbon.bootcamp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GreetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        val name = intent.getStringExtra(EXTRA_MESSAGE)
        val textView = findViewById<TextView>(R.id.greetingMessage).apply {
            text = greetingWithName(name)
        }
    }

    private fun greetingWithName(name: String) =
        """Hello, $name !
            |!!! Welcome to Bootcamp !!!
        """.trimMargin()

}