package com.example.myyandexproject.presentation.audio_player

import android.app.Activity
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
import com.example.myyandexproject.domain.Creator
import com.example.myyandexproject.domain.api.TrackInteractor
import com.example.myyandexproject.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AudioPresenter(private val view : Activity) {

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

    private var track : Track? = null
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private val interactor = Creator.getTracksInteractor()
    }


    fun onCreate(){

        trackTitle = view.findViewById<TextView>(R.id.trackTitle)
        bandTitle =  view.findViewById<TextView>(R.id.bandTitle)
        currentDuration =  view.findViewById<TextView>(R.id.currentDuration)
        durationValue =  view.findViewById<TextView>(R.id.durationValue)
        albumValue =  view.findViewById<TextView>(R.id.albumValue)
        yearValue =  view.findViewById<TextView>(R.id.yearValue)
        genreValue =  view.findViewById<TextView>(R.id.genreValue)
        trackImage =  view.findViewById<ImageView>(R.id.imageView)
        country =  view.findViewById<TextView>(R.id.country)
        btnBack =  view.findViewById(R.id.btnBack)

        val intent = intent
        val bundle = intent.extras
        val track_id = bundle?.getInt("track_id_key")

        if (track_id != null) {
            interactor.getSong(track_id, object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    handler.post{
                        track = foundTracks.first()
                        setValue(track!!)
                    }
                }
            })
        }
        else {
            Toast.makeText(applicationContext, "id трэка не было передано", Toast.LENGTH_SHORT).show()
        }

    }

    fun onDestroy(){

    }

    fun getBigImageUrl(url : String?) : String? {
        return url?.replace("100x100bb.jpg", "512x512bb.jpg")
    }

    fun getYearFromReleaseDate(date : String) : String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        try {
            val date = dateFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val year = calendar.get(Calendar.YEAR)
            return year.toString()
        } catch (e: Exception) {
            return "0"
        }
    }

    fun setValue(track : Track){
        trackTitle.text = track.trackName
        bandTitle.text = track.artistName
        currentDuration.text = "00:30"
        albumValue.text = track.collectionName
        yearValue.text = getYearFromReleaseDate(track.releaseDate)
        genreValue.text = track.primaryGenreName
        country.text = track.country

        durationValue.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis?.toInt())

        Glide.with(trackImage)
            .load(getBigImageUrl(track.artworkUrl100))
            .placeholder(R.drawable.track_image_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .into(trackImage)
    }

}