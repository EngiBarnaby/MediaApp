package com.example.myyandexproject.domain.di

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
}