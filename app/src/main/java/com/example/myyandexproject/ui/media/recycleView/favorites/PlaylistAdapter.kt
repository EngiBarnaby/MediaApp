package com.example.myyandexproject.ui.media.recycleView.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.databinding.PlaylistItemBinding
import com.example.myyandexproject.databinding.TrackItemBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.ui.search.recycleView.TrackClick

class PlaylistAdapter() : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var clickListener : PlaylistClick? = null
    var playlists = emptyList<Playlist>()

    fun setTrackClickListener(listener: PlaylistClick) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])

        holder.itemView.setOnClickListener {
            clickListener?.onClick(playlists[position])
        }
    }

}