package com.example.myyandexproject.domain.models

import com.google.gson.Gson

data class PlaylistTrack(
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

        fun createJsonFromTrack(track: PlaylistTrack): String {
            return Gson().toJson(track)
        }

        fun createTrackFromJson(json: String): PlaylistTrack {
            return Gson().fromJson(json, PlaylistTrack::class.java)
        }

        fun createJsonFromTracksList(tracks: ArrayList<PlaylistTrack>): String {
            return Gson().toJson(tracks)
        }

        fun createTracksListFromJson(json: String): ArrayList<PlaylistTrack> {
            val tracks = Gson().fromJson(json, Array<PlaylistTrack>::class.java)
            return tracks.toCollection(ArrayList())
        }
    }
}
