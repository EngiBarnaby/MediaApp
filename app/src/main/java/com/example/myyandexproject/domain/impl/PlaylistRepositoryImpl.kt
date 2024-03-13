package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.data.converters.PlaylistDbConvertor
import com.example.myyandexproject.data.converters.PlaylistTrackConverter
import com.example.myyandexproject.data.db.AppDatabase
import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackConverter: PlaylistTrackConverter
    ) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistsEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertToPlaylistEntity(playlist))
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, playlistTrackId: Int) {
        appDatabase.playlistDao().addTrackIdToPlaylist(playlistId, playlistTrackId)
    }

    override fun getPlaylistTracksByIds(trackIds: List<Int>): Flow<List<PlaylistTrack>> = flow{
        val tracks = appDatabase.playlistTracks().getTracksByIds(trackIds)
        emit(convertFromTrackEntity(tracks))
    }

    override fun getPlaylistDetails(playlistId: Int): Flow<Playlist> = flow{
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
        emit(playlistDbConvertor.map(playlistEntity))
    }

    override suspend fun removeTrackFromPlaylist(playlistId : Int, playlistTrackIds: List<Int>) {
        appDatabase.playlistDao().removeIdFromPlaylist(playlistId, playlistTrackIds)
    }

    override suspend fun checkPlaylistTrackExistence(playlistTrack: PlaylistTrack) {
        deleteUnnecessaryPlaylistTrack()
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlist.id!!)
        deleteUnnecessaryPlaylistTrack()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(convertToPlaylistEntity(playlist))
    }

    private suspend fun deleteUnnecessaryPlaylistTrack(){
        val playlists = appDatabase.playlistDao().getAllPlaylist()
        val allTracks = appDatabase.playlistTracks().getAllTracks()
        val allTracksIds = allTracks.map { it.id }
        val playlistsTrackIds = playlists.flatMap { it.idList }
        allTracksIds.forEach { trackId ->
            if(!playlistsTrackIds.contains(trackId)){
                appDatabase.playlistTracks().deletePlaylistTrack(trackId)
            }
        }
    }

    private fun convertFromPlaylistsEntity(playlists : List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertToPlaylistEntity(playlist: Playlist) : PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

    private fun convertFromTrackEntity(trackEntities: List<PlaylistTrackEntity>) : List<PlaylistTrack> {
        return trackEntities.map { track -> playlistTrackConverter.map(track) }
    }

}