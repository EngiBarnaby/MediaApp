package com.example.myyandexproject.ui.player.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.utils.convertTime

class AudioPlayerViewModel(
    private var track: Track,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
//    private var mediaPlayer = MediaPlayer()

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
    }

    private fun startChanger(){
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(playerState.value == PlayerState.STATE_PLAYING){
                    changeTime()
                }
                handler.postDelayed(this, TIMER_CHANGE_DELAY_MILLIS)
            }
        }, TIMER_CHANGE_DELAY_MILLIS)
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
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val TIMER_CHANGE_DELAY_MILLIS = 1000L
//        fun getViewModelFactory(track : Track) : ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                AudioPlayerViewModel(track)
//            }
//        }
    }

}