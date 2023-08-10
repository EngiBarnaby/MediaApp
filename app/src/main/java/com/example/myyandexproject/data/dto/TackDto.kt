package com.example.myyandexproject.data.dto

data class TackDto(
    val trackId : Int,
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String
)
