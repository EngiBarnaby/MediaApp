package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface EditPlaylistInteractor {

    fun getPlaylist(playlistId : Int) : Flow<Playlist>

    suspend fun updatePlaylist(playlist: Playlist)

}