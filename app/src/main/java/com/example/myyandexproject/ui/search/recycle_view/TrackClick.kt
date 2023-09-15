package com.example.myyandexproject.ui.search.recycle_view

import com.example.myyandexproject.domain.search.models.Track


interface TrackClick {
    fun onClick(track : Track)
}