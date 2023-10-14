package com.example.myyandexproject.data

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.data.dto.TrackResponse
import com.example.myyandexproject.data.dto.TrackSearchByNameRequest
import com.example.myyandexproject.domain.search.api.TrackRepository
import com.example.myyandexproject.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun getSongs(term: String): Flow<Resource<ArrayList<Track>>> = flow {
        try {
            val response = networkClient.doRequest(TrackSearchByNameRequest(term))
            if(response.resultResponse == 200){
                val tracks = (response as TrackResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it?.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }
                emit(Resource.Success(tracks as ArrayList<Track>))
            }
            else {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
        catch (e : Throwable){
            emit(Resource.Error("Ошибка сервера"))
        }
    }
}