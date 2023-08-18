package com.example.myyandexproject.domain

import com.example.myyandexproject.data.TrackRepositoryImpl
import com.example.myyandexproject.data.network.RetrofitItunesClient
import com.example.myyandexproject.domain.api.TrackInteractor
import com.example.myyandexproject.domain.api.TrackRepository
import com.example.myyandexproject.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository() : TrackRepository{
        return TrackRepositoryImpl(RetrofitItunesClient())
    }

    fun getTracksInteractor() : TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

}