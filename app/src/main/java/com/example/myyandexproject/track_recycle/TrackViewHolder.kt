package com.example.myyandexproject.track_recycle

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R

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
        durationAndTitleBandView.text = "${track.artistName} · ${track.trackTime}"
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .into(trackImageView)
    }

}