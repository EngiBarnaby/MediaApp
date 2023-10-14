package com.example.myyandexproject.data

import com.example.myyandexproject.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto : Any) : Response
}