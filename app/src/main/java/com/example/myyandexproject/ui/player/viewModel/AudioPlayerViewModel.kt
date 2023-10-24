package com.example.myyandexproject.ui.player.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.FavoritesRepository

import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.utils.convertTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private var track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private var timerJob: Job? = null

    private var playerState = MutableLiveData(PlayerState.STATE_DEFAULT)
    private var currentTrackTime = MutableLiveData("00:00")
    private val isFavoriteData = MutableLiveData<Boolean>(false)

    fun getPlayerState() : LiveData<PlayerState> = playerState
    fun getCurrentTrackTime() : LiveData<String> = currentTrackTime
    fun isFavorite() : LiveData<Boolean> = isFavoriteData

    init {
        isFavoriteData.postValue(track.isFavorite)
        preparePlayer()
    }

    fun changeFavoriteStatus() {
        if(isFavoriteData.value == true){
            removeFromFavorite()
            isFavoriteData.postValue(false)
        }
        else if (isFavoriteData.value == false){
            addToFavorite()
            isFavoriteData.postValue(true)
        }
    }


    private fun addToFavorite(){
        viewModelScope.launch {
            favoritesRepository.addTrack(track)
        }
    }

    private fun removeFromFavorite(){
        viewModelScope.launch {
            favoritesRepository.removeTrack(track)
        }
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