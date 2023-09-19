package com.example.myyandexproject

import android.app.Application
import com.example.myyandexproject.data.di.dataModule
import com.example.myyandexproject.domain.di.interactorModule
import com.example.myyandexproject.domain.di.repositoryModule
import com.example.myyandexproject.domain.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}