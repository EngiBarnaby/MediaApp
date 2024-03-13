package com.example.myyandexproject.ui.playlist.recycleView

import com.example.myyandexproject.domain.models.PlaylistTrack

interface PlaylistTrackLongClick {
    fun onLongClick(playlistTrack: PlaylistTrack)

}