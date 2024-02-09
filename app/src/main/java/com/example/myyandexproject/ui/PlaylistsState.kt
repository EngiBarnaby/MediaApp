package com.example.myyandexproject.ui

import com.example.myyandexproject.domain.models.Playlist

sealed interface PlaylistsState {

    object Loading : PlaylistsState

    data class Content(val playlists : List<Playlist>) : PlaylistsState

    data class Empty(val message : String) : PlaylistsState

}