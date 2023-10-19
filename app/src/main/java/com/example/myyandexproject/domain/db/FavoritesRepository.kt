package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun favoritesTracks() : Flow<List<Track>>

    fun getFavoritesTracksSync() : List<Track>

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

}