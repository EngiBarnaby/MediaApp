package com.example.myyandexproject.domain.di

import com.example.myyandexproject.data.FavoritesRepositoryImpl
import com.example.myyandexproject.data.TrackRepositoryImpl
import com.example.myyandexproject.data.converters.TrackDbConvertor
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.search.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    factory {
        TrackDbConvertor()
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

}