package com.example.myyandexproject.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.myyandexproject.ui.media.MediaActivity
import com.example.myyandexproject.R
import com.example.myyandexproject.creator.Creator
import com.example.myyandexproject.ui.search.activity.SearchActivity
import com.example.myyandexproject.ui.settings.activity.SettingsActivity
import com.example.myyandexproject.ui.settings.view_model.SettingsViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val THEME_KEY = "THEME_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val settingBtn = findViewById<Button>(R.id.settingBtn)
        val mediaBtn = findViewById<Button>(R.id.mediaBtn)

        val interactor = Creator.getSettingsInteractor(this)

        val themeState = interactor.getThemeStatus(THEME_KEY)

        if (themeState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


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