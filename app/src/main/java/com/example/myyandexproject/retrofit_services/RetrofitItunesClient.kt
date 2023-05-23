package com.example.myyandexproject.retrofit_services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitItunesClient {
    private var retrofit : Retrofit? = null

    fun getClient() : Retrofit{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}