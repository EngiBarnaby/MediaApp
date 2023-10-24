package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.db.FavoritesInteractor
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.favoritesTracks()
    }

}