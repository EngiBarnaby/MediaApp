package com.example.myyandexproject.data.dto

import com.example.myyandexproject.domain.models.Track

class TrackResponse(
    val resultCount : Int,
    val results : ArrayList<Track>
) : Response()