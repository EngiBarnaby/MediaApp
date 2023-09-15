package com.example.myyandexproject.creator

import android.content.Context
import com.example.myyandexproject.data.TrackRepositoryImpl
import com.example.myyandexproject.data.network.RetrofitItunesClient
import com.example.myyandexproject.data.shared_preferences.SharedPreferencesManager
import com.example.myyandexproject.domain.search.api.TrackRepository
import com.example.myyandexproject.domain.search.SearchInteractorImp
import com.example.myyandexproject.domain.search.api.SearchInteractor
import com.example.myyandexproject.domain.settings.SettingsInteractorImp
import com.example.myyandexproject.domain.settings.api.SettingsInteractor

object Creator {
    private fun getTracksRepository() : TrackRepository {
        return TrackRepositoryImpl(RetrofitItunesClient())
    }

    fun getSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImp(SharedPreferencesManager(context))
    }

    fun getSearchInteractor(context: Context) : SearchInteractor {
        return SearchInteractorImp(
            SharedPreferencesManager(context),
            getTracksRepository()
        )
    }

}