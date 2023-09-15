package com.example.myyandexproject.domain

import android.app.Activity
import android.content.Context
import com.example.myyandexproject.data.TrackRepositoryImpl
import com.example.myyandexproject.data.network.RetrofitItunesClient
import com.example.myyandexproject.domain.api.TrackInteractor
import com.example.myyandexproject.domain.api.TrackRepository
import com.example.myyandexproject.domain.impl.TracksInteractorImpl
import com.example.myyandexproject.presentation.audio_player.AudioPresenter
import com.example.myyandexproject.ui.player.AudioPlayer

object Creator {
    private fun getTracksRepository() : TrackRepository{
        return TrackRepositoryImpl(RetrofitItunesClient())
    }

    fun getTracksInteractor() : TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getAudioPresenter(audioView : AudioPlayer, audioContext : Context) : AudioPresenter {
        return AudioPresenter(view = audioView, context = audioContext)
    }

}