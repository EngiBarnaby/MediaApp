package com.example.myyandexproject.ui.playlist.recycleView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)  {

    private var trackTitleView : TextView
    private var durationAndTitleBandView : TextView
    private var trackImageView : ImageView

    init {
        trackTitleView = itemView.findViewById(R.id.track_title)
        durationAndTitleBandView = itemView.findViewById(R.id.track_duration_and_band_name)
        trackImageView = itemView.findViewById(R.id.track_image)
    }

    fun bind(playlistTrack: PlaylistTrack){
        trackTitleView.text = playlistTrack.trackName
        var time = "0"
        if(playlistTrack.trackTimeMillis != null){
            time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playlistTrack.trackTimeMillis.toInt())
        }
        durationAndTitleBandView.text = "${playlistTrack.artistName} · $time"
        Glide.with(itemView)
            .load(playlistTrack.artworkUrl100)
            .placeholder(R.drawable.track_image_placeholder)
            .override(45, 45)
            .centerCrop()
            .into(trackImageView)
    }

}