package com.example.myyandexproject.ui.playlist.recycleView

import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track


interface PlaylistTrackClick {
    fun onClick(playlistTrack: PlaylistTrack)

}