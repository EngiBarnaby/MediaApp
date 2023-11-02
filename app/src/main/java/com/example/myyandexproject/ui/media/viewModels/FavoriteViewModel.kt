package com.example.myyandexproject.ui.media.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.db.FavoritesInteractor
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.FavoritesState
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val context: Context,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val favoriteState = MutableLiveData<FavoritesState>()

    fun getFavoriteState() : LiveData<FavoritesState> = favoriteState

    init {
        getFavoritesTrack()
    }

    fun getFavoritesTrack() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesInteractor
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