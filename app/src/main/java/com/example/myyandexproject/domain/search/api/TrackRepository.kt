package com.example.myyandexproject.domain.search.api

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getSongs(name : String) : Flow<Resource<ArrayList<Track>>>
}