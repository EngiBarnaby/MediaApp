package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.EditPlaylistInteractor
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class EditPlaylistInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : EditPlaylistInteractor {
    override fun getPlaylist(playlistId: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylistDetails(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsRepository.updatePlaylist(playlist)
    }

}