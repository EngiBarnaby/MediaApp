package com.example.myyandexproject.presentation.audio_player

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.Creator
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.player.AudioPlayer
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.myyandexproject.utils.convertTime

class AudioPresenter(private val view : AudioPlayer, private val context : Context) {

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

    private lateinit var track : Track

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TRACK_DATA = "track_data"
        private const val TIMER_CHANGE_DELAY_MILLIS = 1000L

        private val interactor = Creator.getTracksInteractor()
    }

    fun onCreate() {

        trackTitle = view.findViewById(R.id.trackTitle)
        bandTitle = view.findViewById(R.id.bandTitle)
        currentDuration = view.findViewById(R.id.currentDuration)
        durationValue = view.findViewById(R.id.durationValue)
        albumValue = view.findViewById(R.id.albumValue)
        yearValue = view.findViewById(R.id.yearValue)
        genreValue = view.findViewById(R.id.genreValue)
        trackImage = view.findViewById(R.id.imageView)
        country = view.findViewById(R.id.country)
        btnBack = view.findViewById(R.id.btnBack)
        playBtn = view.findViewById(R.id.fab2)

        btnBack.setOnClickListener {
            view.onBackPressed()
        }

        val intent = view.intent
        val bundle = intent.extras
        val trackData = bundle?.getString(TRACK_DATA)

        if(trackData != null){
            track = Track.createTrackFromJson(trackData)
            setValue(track)
            preparePlayer()
        }

        playBtn.setOnClickListener {
            if(track != null){
                changePlayBtn()
            }
        }

    }


    fun onDestroy() {
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
        currentDuration.text = view.getString(R.string.initial_time)
    }

    private fun setValue(track : Track){
        trackTitle.text = track.trackName
        bandTitle.text = track.artistName
        currentDuration.text = view.getString(R.string.initial_time)
        albumValue.text = track?.collectionName
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