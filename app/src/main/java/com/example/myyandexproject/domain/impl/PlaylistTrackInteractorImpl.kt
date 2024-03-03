package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.PlaylistTrackInteractor
import com.example.myyandexproject.domain.db.PlaylistTracksRepository
import com.example.myyandexproject.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

class PlaylistTrackInteractorImpl(private val playlistTracksRepository: PlaylistTracksRepository) : PlaylistTrackInteractor {
    override fun getPlaylistTracks(): Flow<List<PlaylistTrack>> {
        return playlistTracksRepository.getPlaylistTracks()
    }

    override suspend fun addTrackToPlaylist(playlistTrack: PlaylistTrack) {
        playlistTracksRepository.addTrackToPlaylist(playlistTrack)
    }
}