package com.example.myyandexproject.domain.models

import com.google.gson.Gson

data class Track(
    val trackId : Int,
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String,
    val collectionName : String?,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String,
    val previewUrl : String,
    val isFavorite : Boolean = false
){
    companion object {

        fun createJsonFromTrack(track: Track) : String {
            return Gson().toJson(track)
        }

        fun createTrackFromJson(json : String) : Track {
            return  Gson().fromJson(json, Track::class.java)
        }

        fun createJsonFromTracksList(tracks: ArrayList<Track>): String {
            return Gson().toJson(tracks)
        }

        fun createTracksListFromJson(json : String) : ArrayList<Track> {
            val tracks =  Gson().fromJson(json, Array<Track>::class.java)
            return tracks.toCollection(ArrayList())
        }
    }
}
