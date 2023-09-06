package com.example.myyandexproject.track_recycle

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)  {

    private var trackTitleView : TextView
    private var durationAndTitleBandView : TextView
    private var trackImageView : ImageView

    init {
        trackTitleView = itemView.findViewById(R.id.track_title)
        durationAndTitleBandView = itemView.findViewById(R.id.track_duration_and_band_name)
        trackImageView = itemView.findViewById(R.id.track_image)
    }

    fun bind(track : Track){
        trackTitleView.text = track.trackName
        var time = "0"
        if(track.trackTimeMillis != null){
            time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        }
        durationAndTitleBandView.text = "${track.artistName} · $time"
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder) // Установите радиус закругления, например, 16dp
            .override(45, 45)
            .centerCrop()
            .into(trackImageView)
    }

}