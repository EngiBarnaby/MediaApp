package com.example.myyandexproject.data.converters

import com.example.myyandexproject.data.db.entity.TrackEntity
import com.example.myyandexproject.domain.models.Track

class TrackDbConvertor {

    fun map(track : Track) : TrackEntity{
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate ?: "Нет",
            track.primaryGenreName ?: "Нет",
            track.country,
            track.previewUrl ?: ""
            )
    }


    fun map(track: TrackEntity) : Track {
        return Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate ?: "Нет",
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}