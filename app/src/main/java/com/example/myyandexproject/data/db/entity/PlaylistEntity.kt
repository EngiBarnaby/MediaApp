package com.example.myyandexproject.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myyandexproject.data.converters.IdListConverter

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    val title : String,
    val description : String?,
    val imageUrl : String?,
    val tracksCount : Int,
    @TypeConverters(IdListConverter::class)
    val idList: List<Int>
)