package com.example.myyandexproject.domain.impl

import com.example.myyandexproject.domain.api.TrackInteractor
import com.example.myyandexproject.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun getSong(id: Int, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.getSong(id))
        }
    }
}