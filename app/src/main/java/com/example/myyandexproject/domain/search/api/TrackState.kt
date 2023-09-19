package com.example.myyandexproject.domain.search.api

import com.example.myyandexproject.domain.search.models.Track

sealed interface TrackState {

    object Loading : TrackState

    data class Content(
        val tracks : ArrayList<Track>
    ) : TrackState

    data class Error(
        val errorMessage: String
    ) : TrackState

    data class Empty(
        val message: String
    ) : TrackState

}