package com.example.myyandexproject.data.converters

import com.example.myyandexproject.data.db.entity.PlaylistEntity
import com.example.myyandexproject.domain.models.Playlist

class PlaylistDbConvertor {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            imageUrl = playlist.imageUrl,
            tracksCount = playlist.tracksCount,
            idList = playlist.idList
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            imageUrl = playlistEntity.imageUrl,
            tracksCount = playlistEntity.tracksCount,
            idList = playlistEntity.idList
        )
    }

}