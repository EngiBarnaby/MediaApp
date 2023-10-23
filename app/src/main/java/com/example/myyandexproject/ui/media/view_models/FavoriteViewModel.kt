package com.example.myyandexproject.ui.media.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.FavoritesState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val context: Context,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val favoriteState = MutableLiveData<FavoritesState>()

    fun getFavoriteState() : LiveData<FavoritesState> = favoriteState

    init {
        getFavoritesTrack()
    }

    fun getFavoritesTrack() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesRepository
                .favoritesTracks()
                .collect {tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(movies: List<Track>) {
        if (movies.isEmpty()) {
            renderState(FavoritesState.Empty(context.getString(R.string.media_library_is_empty)))
        } else {
            renderState(FavoritesState.Content(movies))
        }
    }

    private fun renderState(state: FavoritesState){
        favoriteState.postValue(state)
    }
}