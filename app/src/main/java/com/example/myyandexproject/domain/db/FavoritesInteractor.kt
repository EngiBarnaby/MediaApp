package com.example.myyandexproject.domain.db

import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun favoritesTracks() : Flow<List<Track>>

}