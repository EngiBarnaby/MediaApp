package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.data.converters.PlaylistDbConvertor
import com.example.myyandexproject.data.converters.PlaylistTrackConverter
import com.example.myyandexproject.data.db.AppDatabase
import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.db.PlaylistTracksRepository
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConvertor: PlaylistTrackConverter
) : PlaylistTracksRepository {
    override fun getPlaylistTracks(): Flow<List<PlaylistTrack>> = flow{
        val playlistTracks = appDatabase.playlistTracks().getPlaylistTracks()
        emit(convertFromPlaylistTrackEntity(playlistTracks))
    }

    override suspend fun addTrackToPlaylist(track: PlaylistTrack) {
        appDatabase.playlistTracks().insertPlaylistTrack(playlistTrackDbConvertor.map(track))
    }

    private fun convertFromPlaylistTrackEntity(tracks : List<PlaylistTrackEntity>) : List<PlaylistTrack> {
        return tracks.map { track -> playlistTrackDbConvertor.map(track) }
    }

}