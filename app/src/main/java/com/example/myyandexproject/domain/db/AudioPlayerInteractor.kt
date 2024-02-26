package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId : Int)

    fun getPlaylists() : Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: PlaylistTrack)

    fun getPlaylistTracks() : Flow<List<PlaylistTrack>>

}