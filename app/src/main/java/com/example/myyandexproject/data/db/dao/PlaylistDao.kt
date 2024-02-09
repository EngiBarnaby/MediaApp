package com.example.myyandexproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myyandexproject.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT *  FROM playlist_table ORDER BY id DESC")
    suspend fun getPlaylists() : List<PlaylistEntity>

    @Query("UPDATE playlist_table SET idList = idList || :separator || :newId, tracksCount = tracksCount + 1 WHERE id = :playlistId")
    suspend fun addTrackIdToPlaylist(playlistId: Int, newId: Int, separator: String = ",")

}