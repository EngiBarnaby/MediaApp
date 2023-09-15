package com.example.myyandexproject.domain.search.api

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.domain.search.models.Track

interface TrackRepository {
    fun getSongs(name : String) : Resource<ArrayList<Track>>
}