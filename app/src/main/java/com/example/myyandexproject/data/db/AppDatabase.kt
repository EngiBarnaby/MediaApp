package com.example.myyandexproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myyandexproject.data.converters.IdListConverter
import com.example.myyandexproject.data.db.dao.PlaylistDao
import com.example.myyandexproject.data.db.dao.PlaylistTrackDao
import com.example.myyandexproject.data.db.dao.TrackDao
import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity

@Database(version = 10, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
@TypeConverters(IdListConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao() : TrackDao

    abstract fun playlistDao() : PlaylistDao

    abstract fun playlistTracks() : PlaylistTrackDao

}