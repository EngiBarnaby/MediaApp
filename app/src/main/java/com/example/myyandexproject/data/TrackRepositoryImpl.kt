package com.example.myyandexproject.data

import com.example.myyandexproject.data.dto.Resource
import com.example.myyandexproject.data.dto.TrackResponse
import com.example.myyandexproject.data.dto.TrackSearchByNameRequest
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.search.api.TrackRepository
import com.example.myyandexproject.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val favoritesRepository: FavoritesRepository) : TrackRepository {
    override fun getSongs(term: String): Flow<Resource<ArrayList<Track>>> = flow {
        try {
            val response = networkClient.doRequest(TrackSearchByNameRequest(term))
            if(response.resultResponse == 200){
//                val tracks = (response as TrackResponse).results.map {
//                    Track(
//                        trackId = it.trackId,
//                        trackName = it.trackName,
//                        artistName = it.artistName,
//                        trackTimeMillis = it.trackTimeMillis,
//                        artworkUrl100 = it.artworkUrl100,
//                        collectionName = it?.collectionName,
//                        releaseDate = it.releaseDate,
//                        primaryGenreName = it.primaryGenreName,
//                        country = it.country,
//                        previewUrl = it.previewUrl,
//                        isFavorite = favoritesTracks.contains(it.trackId)
//                    )
//                }
                if((response as TrackResponse).results.isNotEmpty()){
                    val tracks = parseTracks(response.results)
                    emit(Resource.Success(tracks as ArrayList<Track>))
                }
            }
            else {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
        catch (e : Throwable){
            emit(Resource.Error("Ошибка сервера"))
        }
    }

    private fun parseTracks(tracks: List<Track>) : List<Track>{
        val favoritesTracks = favoritesRepository.getFavoritesTracksSync().map { track: Track -> track.trackId }
        return tracks.map {
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
                previewUrl = it.previewUrl,
                isFavorite = favoritesTracks.contains(it.trackId)
            )
        }
    }

}