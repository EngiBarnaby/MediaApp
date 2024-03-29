package com.example.myyandexproject.track_recycle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.models.Track

class TrackAdapter(private val tracks : ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    private var clickListener : TrackClick? = null

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