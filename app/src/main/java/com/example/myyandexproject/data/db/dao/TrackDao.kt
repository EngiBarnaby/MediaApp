package com.example.myyandexproject.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myyandexproject.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track : TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracksSuspend() : List<TrackEntity>

    @Query("SELECT * FROM track_table")
    fun getTracks() : List<TrackEntity>
}