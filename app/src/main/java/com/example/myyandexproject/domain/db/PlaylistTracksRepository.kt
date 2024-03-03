package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface PlaylistTracksRepository {

    fun getPlaylistTracks() : Flow<List<PlaylistTrack>>

    suspend fun addTrackToPlaylist(track: PlaylistTrack)

}