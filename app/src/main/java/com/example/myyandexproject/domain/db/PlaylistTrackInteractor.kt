package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface PlaylistTrackInteractor {

    fun getPlaylistTracks() : Flow<List<PlaylistTrack>>

    suspend fun addTrackToPlaylist(playlistTrack: PlaylistTrack)

}