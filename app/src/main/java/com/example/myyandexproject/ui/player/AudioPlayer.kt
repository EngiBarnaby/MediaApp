package com.example.myyandexproject.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.Creator
import com.example.myyandexproject.presentation.audio_player.AudioPresenter

class AudioPlayer : AppCompatActivity() {

    private lateinit var btnBack : TextView
    private lateinit var audioPresenter : AudioPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_audio_player)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        audioPresenter = Creator.getAudioPresenter(this, this)
        audioPresenter.onCreate()

    }

    override fun onDestroy() {
        super.onDestroy()
        audioPresenter.onDestroy()
    }
}