package com.example.myyandexproject.domain.api

import com.example.myyandexproject.domain.models.Track

interface TrackInteractor {
    fun getSong(id : Int, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks : List<Track>)
    }

}