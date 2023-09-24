package com.example.myyandexproject.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.example.myyandexproject.ui.media.MediaActivity
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.settings.api.SettingsInteractor
import com.example.myyandexproject.ui.search.activity.SearchActivity
import com.example.myyandexproject.ui.settings.activity.SettingsActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val settingBtn = findViewById<Button>(R.id.settingBtn)
        val mediaBtn = findViewById<Button>(R.id.mediaBtn)


        searchBtn.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        mediaBtn.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        settingBtn.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}