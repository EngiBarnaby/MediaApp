package com.example.myyandexproject.repository

import com.example.myyandexproject.domain.models.Track

class TrackResponse(
    val resultCount : Int,
    val results : ArrayList<Track>
)