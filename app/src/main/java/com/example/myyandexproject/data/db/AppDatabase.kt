package com.example.myyandexproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myyandexproject.data.db.dao.PlaylistDao
import com.example.myyandexproject.data.db.dao.TrackDao
import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.data.db.entity.TrackEntity

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao() : TrackDao

    abstract fun playlistDao() : PlaylistDao

}