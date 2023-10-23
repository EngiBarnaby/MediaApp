package com.example.myyandexproject.ui.media.recycle_view.favorites

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.TrackItemBinding
import com.example.myyandexproject.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteViewHolder(private val binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackTitle.text = track.trackName
        var time = "0"
        if (track.trackTimeMillis != null) {
            time =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        }
        binding.trackDurationAndBandName.text = "${track.artistName} · $time"
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_image_placeholder)
            .override(45, 45)
            .centerCrop()
            .into(binding.trackImage)
    }

}