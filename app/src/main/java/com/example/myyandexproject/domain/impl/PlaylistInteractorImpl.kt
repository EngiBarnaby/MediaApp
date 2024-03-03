package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.PlaylistInteractor
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId: Int) {
        playlistsRepository.addTrackToPlaylist(playlistId, playlistTrackId)
    }
}