package com.example.myyandexproject.ui.player

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R
import com.example.myyandexproject.data.network.ItunesApi
import com.example.myyandexproject.domain.Creator
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.repository.TrackResponse
import com.example.myyandexproject.retrofit_services.RetrofitItunesClient
import com.example.myyandexproject.utils.SUCCESS_RESPONSE
import com.example.myyandexproject.utils.convertTime
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    companion object {
        private val presenter = Creator.getTracksInteractor()
    }

    private lateinit var btnBack : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}