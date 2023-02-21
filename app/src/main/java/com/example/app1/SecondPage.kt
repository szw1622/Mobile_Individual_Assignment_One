package com.example.app1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        val fullNameTextView: TextView = findViewById(R.id.full_name_textview)

        val fullName = intent.getStringExtra("fullName")
        fullName?.let {
            val names = it.split(" ")
            var message = "${names[0]} "
            message += if (names.size > 2) {
                "${names.last()}"
            } else if (names.size > 1) {
                "${names[1]}"
            } else {
                ""
            }
            if (message.isNotEmpty()) {
                message += " is logged in!"
            }
            fullNameTextView.text = message
        }



        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}

