package com.example.myyandexproject.data

import com.example.myyandexproject.data.dto.TrackResponse
import com.example.myyandexproject.data.dto.TrackSearchByIdRequest
import com.example.myyandexproject.domain.api.TrackRepository
import com.example.myyandexproject.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun getSong(id: Int): List<Track> {
        val response = networkClient.doRequest(TrackSearchByIdRequest(id))
        if(response.resultResponse == 200){
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        }
        else {
            return emptyList()
        }
    }
}