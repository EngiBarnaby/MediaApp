package com.example.myyandexproject.data.converters

import com.example.myyandexproject.data.db.entity.PlaylistTrackEntity
import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.domain.models.Track

class PlaylistTrackConverter {
    fun map(track : PlaylistTrack) : PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }


    fun map(track: PlaylistTrackEntity) : PlaylistTrack {
        return PlaylistTrack(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}