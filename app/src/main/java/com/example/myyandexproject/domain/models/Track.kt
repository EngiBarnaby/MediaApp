package com.example.myyandexproject.domain.models

import com.google.gson.Gson

data class Track(
    val trackId : Int,
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String
){
    companion object {
        fun createJsonFromTracksList(tracks: ArrayList<Track>): String {
            return Gson().toJson(tracks)
        }

        fun createTracksListFromJson(json : String) : Array<Track> {
            return  Gson().fromJson(json, Array<Track>::class.java)
        }
    }
}
