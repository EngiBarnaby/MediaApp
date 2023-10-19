package com.example.myyandexproject.data.di

import androidx.room.Room
import com.example.myyandexproject.data.network.ItunesApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.myyandexproject.data.NetworkClient
import com.example.myyandexproject.data.db.AppDatabase
import com.example.myyandexproject.data.network.RetrofitItunesClient
import com.example.myyandexproject.data.shared_preferences.SharedPreferencesClient
import com.example.myyandexproject.data.shared_preferences.SharedPreferencesManager
import org.koin.android.ext.koin.androidContext

val dataModule = module {
    single<ItunesApi>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single<NetworkClient>{
        RetrofitItunesClient(get())
    }

    single<SharedPreferencesClient>{
        SharedPreferencesManager(androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

}