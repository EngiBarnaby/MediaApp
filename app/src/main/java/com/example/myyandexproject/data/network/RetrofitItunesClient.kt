package com.example.myyandexproject.data.network

import com.example.myyandexproject.data.NetworkClient
import com.example.myyandexproject.data.dto.Response
import com.example.myyandexproject.data.dto.TrackSearchByIdRequest
import com.example.myyandexproject.data.dto.TrackSearchByNameRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class RetrofitItunesClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private var retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchByNameRequest){
                val response = itunesService.searchSongs(dto.term).execute()
                val body = response.body() ?: Response()
                return body.apply { resultResponse = response.code() }
            }
            else{
                return Response().apply { resultResponse = 400 }
            }
        }
        catch (e : UnknownHostException){
            return  Response().apply { resultResponse = 204 }
        }
    }
}