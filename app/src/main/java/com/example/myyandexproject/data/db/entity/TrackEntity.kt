package com.example.myyandexproject.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val id : Int,
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String,
    val collectionName : String?,
    val releaseDate : String?,
    val primaryGenreName : String?,
    val country : String,
    val previewUrl : String,
    @ColumnInfo(name = "timestamp_added")
    val timestampAdded: Long = System.currentTimeMillis()
)
