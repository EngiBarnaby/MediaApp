package com.example.myyandexproject.data

import com.example.myyandexproject.data.converters.TrackDbConvertor
import com.example.myyandexproject.data.db.AppDatabase
import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor) : FavoritesRepository {

    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracksSuspend()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavoritesTracksSync(): List<Track> {
        return convertFromTrackEntity(appDatabase.trackDao().getTracks())
    }

    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().insertTrack(convertToTrackEntity(track))
    }

    override suspend fun removeTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(convertToTrackEntity(track))
    }

    private fun convertToTrackEntity(track : Track) : TrackEntity{
        return trackDbConvertor.map(track)
    }

    private fun convertFromTrackEntity(tracks : List<TrackEntity>) : List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}