package com.example.myyandexproject

import com.example.myyandexproject.data.converters.PlaylistDbConvertor
import com.example.myyandexproject.data.db.AppDatabase
import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
    ) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistsEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertToPlaylistEntity(playlist))
    }

    fun convertFromPlaylistsEntity(playlists : List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    fun convertToPlaylistEntity(playlist: Playlist) : PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

}