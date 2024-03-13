package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.PlaylistDetailInteractor
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

class PlaylistDetailInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistDetailInteractor {
    override fun getPlaylistDetails(playlistId: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylistDetails(playlistId)
    }

    override fun getPlaylistTracksByIds(trackIds: List<Int>) : Flow<List<PlaylistTrack>> {
        return playlistsRepository.getPlaylistTracksByIds(trackIds)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Int, playlistTrackIds: List<Int>) {
        return playlistsRepository.removeTrackFromPlaylist(playlistId, playlistTrackIds)
    }

    override suspend fun checkPlaylistTrackExistence(playlistTrack: PlaylistTrack) {
        playlistsRepository.checkPlaylistTrackExistence(playlistTrack)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }
}