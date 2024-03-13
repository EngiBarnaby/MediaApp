package com.example.myyandexproject.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack : PlaylistTrackEntity)

    @Query("DELETE FROM playlist_track_table WHERE id = :id")
    suspend fun deletePlaylistTrack(id : Int)

    @Query("SELECT * FROM playlist_track_table ORDER BY timestamp_added DESC")
    suspend fun getPlaylistTracks() : List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_track_table WHERE id IN (:trackIds)")
    suspend fun getTracksByIds(trackIds : List<Int>) : List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_track_table")
    suspend fun getAllTracks() : List<PlaylistTrackEntity>

}