package com.example.myyandexproject.data.network

import com.example.myyandexproject.data.NetworkClient
import com.example.myyandexproject.data.dto.Response
import com.example.myyandexproject.data.dto.TrackSearchByNameRequest
import java.net.UnknownHostException

class RetrofitItunesClient(private val itunesService : ItunesApi) : NetworkClient {

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