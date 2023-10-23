package com.example.myyandexproject.ui.media.recycle_view.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.databinding.TrackItemBinding
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.search.recycle_view.TrackClick

class FavoriteAdapter : RecyclerView.Adapter<FavoriteViewHolder>() {

    private var clickListener : TrackClick? = null
    var tracks = emptyList<Track>()

    fun setTrackClickListener(listener: TrackClick) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener?.onClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}