package com.example.myyandexproject.ui.player

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myyandexproject.R
import com.example.myyandexproject.SearchActivity

class TrackError : AppCompatActivity() {

    lateinit var btnBack : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_error)

        btnBack = findViewById(R.id.btnBackToSearch)

        btnBack.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
    }
}