package com.example.myyandexproject.retrofit_services

import com.example.myyandexproject.repository.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    fun searchSong(@Query("term") term : String) : Call<TrackResponse>

}