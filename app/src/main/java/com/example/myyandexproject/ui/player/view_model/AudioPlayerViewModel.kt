package com.example.myyandexproject.ui.player.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.utils.convertTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private var track: Track,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private var timerJob: Job? = null

    private var playerState = MutableLiveData(PlayerState.STATE_DEFAULT)
    private var currentTrackTime = MutableLiveData("00:00")

    fun getPlayerState() : LiveData<PlayerState> = playerState
    fun getCurrentTrackTime() : LiveData<String> = currentTrackTime

    init {
        preparePlayer()
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState.value = PlayerState.STATE_PLAYING
        startChanger()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState.value = PlayerState.STATE_PAUSED
        timerJob?.cancel()
    }

    fun preparedPlayer(){
        timerJob?.cancel()
        currentTrackTime.value = "00:00"
    }

    private fun startChanger(){
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying){
                delay(300L)
                changeTime()
            }
        }
    }

    private fun changeTime(){
        currentTrackTime.value = convertTime(mediaPlayer.currentPosition)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            setListenerData()
        }
        mediaPlayer.setOnCompletionListener {
            setListenerData()
        }
    }

    private fun setListenerData(){
        playerState.value = PlayerState.STATE_PREPARED
    }

    override fun onCleared() {
        mediaPlayer.release()
    }
}