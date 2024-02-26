package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.AudioPlayerInteractor
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.db.PlaylistTracksRepository
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

class AudioPlayerInteractorImpl(
    private val favoritesRepository: FavoritesRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistTracksRepository: PlaylistTracksRepository
) : AudioPlayerInteractor {
    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        favoritesRepository.removeTrack(track)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId: Int) {
        playlistsRepository.addTrackToPlaylist(playlistId, playlistTrackId)
    }

    override suspend fun addTrackToPlaylist(track: PlaylistTrack) {
        playlistTracksRepository.addTrackToPlaylist(track)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun getPlaylistTracks(): Flow<List<PlaylistTrack>> {
        return playlistTracksRepository.getPlaylistTracks()
    }
}