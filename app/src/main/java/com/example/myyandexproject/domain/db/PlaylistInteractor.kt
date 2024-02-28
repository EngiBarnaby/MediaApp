package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists() : Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId : Int)
}