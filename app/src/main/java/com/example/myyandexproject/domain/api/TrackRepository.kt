package com.example.myyandexproject.domain.api

import com.example.myyandexproject.domain.models.Track

interface TrackRepository {
    fun getSong(id : Int) : List<Track>
}