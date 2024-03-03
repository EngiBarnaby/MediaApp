package com.example.myyandexproject.data.network

import android.util.Log
import com.example.myyandexproject.data.NetworkClient
import com.example.myyandexproject.data.dto.Response
import com.example.myyandexproject.data.dto.TrackSearchByNameRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class RetrofitItunesClient(private val itunesService : ItunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchByNameRequest){
                return withContext(Dispatchers.IO){
                    try{
                        val response = itunesService.searchSongs(dto.term)
                        response.apply { resultResponse = 200 }
                    }
                    catch (e : Throwable){
                        Log.e("error", e.toString())
                        Response().apply { resultResponse = 500 }
                    }
                }
            }
            else{
                return Response().apply { resultResponse = 400 }
            }
        }
        catch (e : UnknownHostException){
            Log.e("error", e.toString())
            return  Response().apply { resultResponse = 204 }
        }
    }
}