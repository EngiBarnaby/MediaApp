package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface PlaylistDetailInteractor {

    fun getPlaylistDetails(playlistId : Int) : Flow<Playlist>

    fun getPlaylistTracksByIds(trackIds : List<Int>) : Flow<List<PlaylistTrack>>

    suspend fun removeTrackFromPlaylist(playlistId : Int, playlistTrackIds : List<Int>)

    suspend fun checkPlaylistTrackExistence(playlistTrack: PlaylistTrack)

    suspend fun deletePlaylist(playlist : Playlist)
}