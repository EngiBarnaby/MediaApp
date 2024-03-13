package com.example.myyandexproject.domain.models

import com.google.gson.Gson

data class Playlist(
    val id : Int?,
    val title : String,
    val description : String?,
    val imageUrl: String?,
    val tracksCount : Int,
    var idList : List<Int>
){
    companion object {
        fun createJsonFromPlaylist(playlist: Playlist) : String {
            return Gson().toJson(playlist)
        }

        fun createPlaylistFromJson(json : String) : Playlist {
            return  Gson().fromJson(json, Playlist::class.java)
        }
    }
}
