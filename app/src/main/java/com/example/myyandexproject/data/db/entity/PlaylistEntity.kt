package com.example.myyandexproject.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String?,
    val imageUrl : String?
)
