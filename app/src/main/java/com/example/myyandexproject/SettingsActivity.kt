package com.example.myyandexproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<TextView>(R.id.btnBack)

        btnBack.setOnClickListener {
            super.onBackPressed();
//            val displayIntent = Intent(this, SettingsActivity::class.java)
//            startActivity(displayIntent)
        }

    }
}