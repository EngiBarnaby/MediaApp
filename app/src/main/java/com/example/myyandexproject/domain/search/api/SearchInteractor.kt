package com.example.myyandexproject.domain.search.api

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun getHistoryTracksFromShared(key : String) : String?

    fun setHistoryTracksToShared(key: String, value : String)

    fun getSearchText(key: String) : String

    fun setSearchText(key: String, value: String)

    fun getSongs(name : String) : Flow<Resource<ArrayList<Track>>>

}