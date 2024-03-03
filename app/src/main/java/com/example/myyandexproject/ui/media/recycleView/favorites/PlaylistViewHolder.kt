package com.example.myyandexproject.ui.media.recycleView.favorites

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.PlaylistItemBinding
import com.example.myyandexproject.domain.models.Playlist

class PlaylistViewHolder(private val binding : PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist){
            binding.playlistTitle.text = playlist.title
            binding.playlistTracksCount.text = "${playlist.tracksCount.toString()} ${if (playlist.tracksCount == 1) "трек" else "треков"}"
            Log.i("playlist", playlist.imageUrl.toString())
            Glide.with(itemView)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.track_image_placeholder)
                .override(160, 160)
                .centerCrop()
                .into(binding.playlistImage)
        }
}