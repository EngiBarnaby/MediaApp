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
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TRACK_ID_KEY = "track_id_key"
        private const val TIMER_CHANGE_DELAY_MILLIS = 1000L
    }

    private val retrofitClient = RetrofitItunesClient.getClient()
    private val itunesService = retrofitClient.create(ItunesApi::class.java)
    private lateinit var trackTitle : TextView
    private lateinit var bandTitle : TextView
    private lateinit var currentDuration : TextView
    private lateinit var durationValue : TextView
    private lateinit var albumValue : TextView
    private lateinit var yearValue : TextView
    private lateinit var genreValue : TextView
    private lateinit var trackImage : ImageView
    private lateinit var country : TextView
    private lateinit var btnBack : TextView
    private lateinit var playBtn : MaterialButton

    private var track : Track? = null

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        trackTitle = findViewById<TextView>(R.id.trackTitle)
        bandTitle = findViewById<TextView>(R.id.bandTitle)
        currentDuration = findViewById<TextView>(R.id.currentDuration)
        durationValue = findViewById<TextView>(R.id.durationValue)
        albumValue = findViewById<TextView>(R.id.albumValue)
        yearValue = findViewById<TextView>(R.id.yearValue)
        genreValue = findViewById<TextView>(R.id.genreValue)
        trackImage = findViewById<ImageView>(R.id.imageView)
        country = findViewById<TextView>(R.id.country)
        btnBack = findViewById(R.id.btnBack)
        playBtn = findViewById(R.id.fab2)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        val intent = intent
        val bundle = intent.extras
        val trackId = bundle?.getInt(TRACK_ID_KEY)

        if (trackId != null) {
            getSong(trackId)
        }
        else{
            Toast.makeText(applicationContext, "id тркека не было передано", Toast.LENGTH_SHORT).show()
        }

        playBtn.setOnClickListener {
            if(track != null){
                changePlayBtn()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun startChanger(){
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(playerState == STATE_PLAYING){
                    changeTime()
                }
                handler.postDelayed(this, TIMER_CHANGE_DELAY_MILLIS)
            }
        }, TIMER_CHANGE_DELAY_MILLIS)
    }

    private fun changeTime(){
        currentDuration.text = convertTime(mediaPlayer.currentPosition)
    }

    private fun getSong(id : Int){
    }

    private fun getBigImageUrl(url : String?) : String? {
        return url?.replace("100x100bb.jpg", "512x512bb.jpg")
    }

    private fun getYearFromReleaseDate(date : String) : String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        try {
            val formatDate = dateFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = formatDate!!

            val year = calendar.get(Calendar.YEAR)
            return year.toString()
        } catch (e: Exception) {
            return "0"
        }
    }

    private fun changePlayBtn(){
        when(playerState){
            STATE_DEFAULT, STATE_PREPARED, STATE_PAUSED -> {
                playBtn.setIconResource(R.drawable.baseline_pause_24)
                startPlayer()
            }
            STATE_PLAYING -> {
                playBtn.setIconResource(R.drawable.baseline_play_arrow_24)
                pausePlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        startChanger()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playBtn.setIconResource(R.drawable.baseline_play_arrow_24)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            setListenerData()
        }
        mediaPlayer.setOnCompletionListener {
            setListenerData()
        }
    }

    private fun setListenerData(){
        playerState = STATE_PREPARED
        playBtn.setIconResource(R.drawable.baseline_play_arrow_24)
        currentDuration.text = getString(R.string.initial_time)
    }

    private fun setValue(track : Track){
        trackTitle.text = track.trackName
        bandTitle.text = track.artistName
        currentDuration.text = getString(R.string.initial_time)
        albumValue.text = track.collectionName
        yearValue.text = getYearFromReleaseDate(track.releaseDate)
        genreValue.text = track.primaryGenreName
        country.text = track.country

        durationValue.text = convertTime(track.trackTimeMillis?.toInt())

        Glide.with(trackImage)
            .load(getBigImageUrl(track.artworkUrl100))
            .placeholder(R.drawable.track_image_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .into(trackImage)
    }

}