package com.example.myyandexproject.ui.player.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.databinding.PlaylistVerticalItemBinding
import com.example.myyandexproject.domain.models.Playlist

class PlaylistVerticalAdapter() : RecyclerView.Adapter<PlaylistVerticalViewHolder>() {

    private var clickListener : PlaylistVerticalClick? = null
    var playlists = emptyList<Playlist>()

    fun setTrackClickListener(listener: PlaylistVerticalClick) {
        clickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVerticalViewHolder {
        val binding = PlaylistVerticalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistVerticalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistVerticalViewHolder, position: Int) {
        holder.bind(playlists[position])

        holder.itemView.setOnClickListener {
            clickListener?.onClick(playlists[position])
        }
    }

}