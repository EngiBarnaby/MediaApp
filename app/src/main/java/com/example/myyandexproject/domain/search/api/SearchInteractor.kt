package com.example.myyandexproject.domain.search.api

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.domain.search.models.Track

interface SearchInteractor {

    fun getHistoryTracksFromShared(key : String) : String?

    fun setHistoryTracksToShared(key: String, value : String)

    fun getSearchText(key: String) : String

    fun setSearchText(key: String, value: String)

    fun getSongs(name : String) : Resource<ArrayList<Track>>

}