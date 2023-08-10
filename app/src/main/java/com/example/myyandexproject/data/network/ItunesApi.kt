package com.example.myyandexproject.data.network

import com.example.myyandexproject.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    fun searchSongs(@Query("term") term : String) : Call<TrackResponse>

    @GET("lookup")
    fun getSongById(@Query("id") id : Int) : Call<TrackResponse>

}