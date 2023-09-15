package com.example.myyandexproject.ui.search.recycle_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.search.models.Track

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    private var clickListener : TrackClick? = null
    var tracks = ArrayList<Track>()

    fun setTrackClickListener(listener: TrackClick) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener?.onClick(tracks[position])
        }
    }
}