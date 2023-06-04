package com.example.myyandexproject.repository

import com.google.gson.Gson

data class Track(
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String
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
