package com.example.myyandexproject.domain.di

import com.example.myyandexproject.data.FavoritesRepositoryImpl
import com.example.myyandexproject.data.TrackRepositoryImpl
import com.example.myyandexproject.data.converters.PlaylistDbConvertor
import com.example.myyandexproject.data.converters.PlaylistTrackConverter
import com.example.myyandexproject.data.converters.TrackDbConvertor
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.db.PlaylistTracksRepository
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.impl.PlaylistRepositoryImpl
import com.example.myyandexproject.domain.impl.PlaylistTrackRepositoryImpl
import com.example.myyandexproject.domain.search.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    single<PlaylistTracksRepository> {
        PlaylistTrackRepositoryImpl()
    }

    factory {
        TrackDbConvertor()
    }

    factory {
        PlaylistDbConvertor()
    }

    factory {
        PlaylistTrackConverter()
    }

}