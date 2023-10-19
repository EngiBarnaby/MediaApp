package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun tracksFavorites() : Flow<List<Track>>

}