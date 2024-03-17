package com.example.myyandexproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.domain.models.Playlist

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT *  FROM playlist_table ORDER BY id DESC")
    suspend fun getPlaylists() : List<PlaylistEntity>

    @Query("UPDATE playlist_table SET idList = idList || :separator || :newId, tracksCount = tracksCount + 1 WHERE id = :playlistId")
    suspend fun addTrackIdToPlaylist(playlistId: Int, newId: Int, separator: String = ",")

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id : Int) : PlaylistEntity

    @Query("UPDATE playlist_table SET idList = :newIdList WHERE id = :playlistId")
    suspend fun removeIdFromPlaylist(playlistId: Int, newIdList: List<Int>)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist() : List<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylist(id : Int)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

}