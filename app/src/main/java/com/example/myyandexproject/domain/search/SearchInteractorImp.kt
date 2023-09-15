package com.example.myyandexproject.domain.search

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.data.shared_preferences.SharedPreferencesClient
import com.example.myyandexproject.domain.search.api.TrackRepository
import com.example.myyandexproject.domain.search.api.SearchInteractor
import com.example.myyandexproject.domain.search.models.Track
import java.util.concurrent.Executors

class SearchInteractorImp(
    private val sharedManager: SharedPreferencesClient,
    private val repository: TrackRepository
) : SearchInteractor {

    override fun getHistoryTracksFromShared(key: String): String {
        return sharedManager.getStringValue(key)
    }

    override fun setHistoryTracksToShared(key: String, value: String) {
        sharedManager.setStringValue(key, value)
    }

    override fun getSearchText(key: String): String {
        return sharedManager.getStringValue(key)
    }

    override fun setSearchText(key: String, value: String) {
        sharedManager.setStringValue(key, value)
    }

    override fun getSongs(term: String) : Resource<ArrayList<Track>> {
        return repository.getSongs(term)
    }
}