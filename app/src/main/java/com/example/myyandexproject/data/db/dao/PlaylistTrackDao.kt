package com.example.myyandexproject.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack : PlaylistTrackEntity)

    @Delete
    suspend fun deletePlaylistTrack(playlistTrack : PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table ORDER BY timestamp_added DESC")
    suspend fun getPlaylistTracks() : List<PlaylistTrackEntity>
}