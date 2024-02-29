package com.example.myyandexproject.domain.di

import com.example.myyandexproject.domain.db.AudioPlayerInteractor
import com.example.myyandexproject.domain.db.FavoritesInteractor
import com.example.myyandexproject.domain.db.PlaylistInteractor
import com.example.myyandexproject.domain.db.PlaylistTrackInteractor
import com.example.myyandexproject.domain.impl.AudioPlayerInteractorImpl
import com.example.myyandexproject.domain.impl.FavoritesInteractorImpl
import com.example.myyandexproject.domain.impl.PlaylistInteractorImpl
import com.example.myyandexproject.domain.impl.PlaylistTrackInteractorImpl
import com.example.myyandexproject.domain.search.SearchInteractorImp
import com.example.myyandexproject.domain.search.api.SearchInteractor
import com.example.myyandexproject.domain.settings.SettingsInteractorImp
import com.example.myyandexproject.domain.settings.api.SettingsInteractor
import org.koin.dsl.module

val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImp(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImp(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistTrackInteractor> {
        PlaylistTrackInteractorImpl(get())
    }

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get(), get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}