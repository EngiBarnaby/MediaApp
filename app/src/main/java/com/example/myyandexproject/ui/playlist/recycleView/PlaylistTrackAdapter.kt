package com.example.myyandexproject.ui.playlist.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.models.PlaylistTrack

class PlaylistTrackAdapter() : RecyclerView.Adapter<PlaylistTrackViewHolder>(){

    private var clickListener : PlaylistTrackClick? = null
    private var longClickListener : PlaylistTrackLongClick? = null
    var playlistTracks = emptyList<PlaylistTrack>()

    fun setTrackClickListener(listener: PlaylistTrackClick) {
        clickListener = listener
    }
    
    fun setTrackLongClick(listener: PlaylistTrackLongClick){
        longClickListener = listener
    }


    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistTracks.size
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(playlistTracks[position])

        holder.itemView.setOnClickListener {
            clickListener?.onClick(playlistTracks[position])
        }

        holder.itemView.setOnLongClickListener {
            longClickListener?.onLongClick(playlistTracks[position])
            return@setOnLongClickListener true
        }
    }
}