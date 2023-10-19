package com.example.myyandexproject.ui

import com.example.myyandexproject.domain.models.Track

sealed interface FavoritesState {

    object Loading : FavoritesState

    data class Content(val tracks : List<Track>) : FavoritesState

    data class Empty(val message : String) : FavoritesState

}
