package com.example.myyandexproject.ui.player.recycleView

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.PlaylistVerticalItemBinding
import com.example.myyandexproject.domain.models.Playlist

class PlaylistVerticalViewHolder(private val binding: PlaylistVerticalItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist){
        binding.playlistTitle.text = playlist.title
        binding.playlistTrackCount.text = "${playlist.tracksCount.toString()} ${if (playlist.tracksCount == 1) "трек" else "треков"}"
        Log.i("playlist", playlist.imageUrl.toString())
        Glide.with(itemView)
            .load(playlist.imageUrl)
            .placeholder(R.drawable.track_image_placeholder)
            .override(45, 45)
            .centerCrop()
            .into(binding.playlistImage)
    }

}