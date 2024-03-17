package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getPlaylists() : Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId : Int)

    fun getPlaylistTracksByIds(trackIds : List<Int>) : Flow<List<PlaylistTrack>>

    fun getPlaylistDetails(playlistId : Int) : Flow<Playlist>

    suspend fun removeTrackFromPlaylist(playlistId : Int, playlistTrackIds : List<Int>)

    suspend fun checkPlaylistTrackExistence(playlistTrack: PlaylistTrack)

    suspend fun deletePlaylist(playlist : Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

}