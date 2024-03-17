package com.example.myyandexproject.ui.test

import com.example.myyandexproject.domain.models.PlaylistTrack

sealed interface PlaylistTracksState {
        object Loading : PlaylistTracksState

        data class Content(var playlistTracks : List<PlaylistTrack>) : PlaylistTracksState

        data class Empty(val message : String) : PlaylistTracksState

}