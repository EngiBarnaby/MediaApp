package com.example.myyandexproject.ui.media.recycleView.favorites

import com.example.myyandexproject.domain.models.Playlist

interface PlaylistClick {
    fun onClick(playlist : Playlist)
}