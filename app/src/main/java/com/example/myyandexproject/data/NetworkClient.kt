package com.example.myyandexproject.data

import com.example.myyandexproject.data.dto.Response

interface NetworkClient {
    fun doRequest(dto : Any) : Response
}