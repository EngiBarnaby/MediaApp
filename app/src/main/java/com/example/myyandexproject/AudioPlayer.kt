package com.example.myyandexproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.repository.Track
import com.example.myyandexproject.repository.TrackResponse
import com.example.myyandexproject.retrofit_services.ItunesApi
import com.example.myyandexproject.retrofit_services.RetrofitItunesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

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

    private var track : Track? = null

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

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        val intent = intent
        val bundle = intent.extras
        val track_id = bundle?.getInt("track_id_key")

        if (track_id != null) {
            getSong(track_id)
        }
        else{
            Toast.makeText(applicationContext, "id трэка не было передано", Toast.LENGTH_SHORT).show()
        }
    }

    fun getSong(id : Int){
        itunesService.getSongById(id).enqueue( object : Callback<TrackResponse>{
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if(response.code() == 200){
                    if (response.body()?.results?.isNotEmpty() == true) {
                        track = response.body()?.results!![0]
                        setValue(track!!)
                    }
                    else {
                        Toast.makeText(applicationContext, "Трэк с этим id не был найден", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                Toast.makeText(applicationContext, resources.getString(R.string.failure_get_song), Toast.LENGTH_SHORT).show()
            }
        })
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