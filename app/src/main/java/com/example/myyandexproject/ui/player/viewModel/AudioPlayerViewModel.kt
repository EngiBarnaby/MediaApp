package com.example.myyandexproject.ui.player.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.AudioPlayerInteractor
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack

import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.PlaylistsState
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.utils.convertTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private var track: Track,
    private val mediaPlayer: MediaPlayer,
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private var playerState = MutableLiveData(PlayerState.STATE_DEFAULT)
    private var currentTrackTime = MutableLiveData("00:00")
    private val isFavoriteData = MutableLiveData<Boolean>(false)
    private val playListsState = MutableLiveData<PlaylistsState>()
    private val playListTracks = MutableLiveData<List<PlaylistTrack>>()

    fun getPlayerState() : LiveData<PlayerState> = playerState
    fun getCurrentTrackTime() : LiveData<String> = currentTrackTime
    fun isFavorite() : LiveData<Boolean> = isFavoriteData

    fun getPlayListsState() : LiveData<PlaylistsState> = playListsState

    init {
        isFavoriteData.postValue(track.isFavorite)
        preparePlayer()
        fetchPlayList()
        fetchPlaylistTracks()
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

    fun addTrackInPlaylist(track: Track, playlist : Playlist){
        val playlistTrack = PlaylistTrack(
            trackId=track.trackId,
            trackName=track.trackName,
            artistName=track.artistName,
            trackTimeMillis=track.trackTimeMillis,
            artworkUrl100=track.artworkUrl100,
            collectionName=track.collectionName,
            releaseDate=track.releaseDate ?: "Нет",
            primaryGenreName=track.primaryGenreName ?: "Нет",
            country=track.country,
            previewUrl=track.previewUrl ?: ""
        )

        if (!playListTracks.value?.contains(playlistTrack)!!){
            viewModelScope.launch {
                audioPlayerInteractor.addTrackToPlaylist(playlistTrack)
            }
        }

        viewModelScope.launch {
            audioPlayerInteractor.addTrackToPlaylist(playlist.id!!, track.trackId)
            playListsState.value = PlaylistsState.Loading
            audioPlayerInteractor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun fetchPlaylistTracks(){
        viewModelScope.launch {
            audioPlayerInteractor.getPlaylistTracks()
                .collect(){
                    playListTracks.value = it
                }
        }
    }

    private fun addToFavorite(){
        viewModelScope.launch {
            audioPlayerInteractor.addTrack(track)
        }
    }

    private fun removeFromFavorite(){
        viewModelScope.launch {
            audioPlayerInteractor.removeTrack(track)
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

    fun fetchPlayList(){

        viewModelScope.launch(Dispatchers.IO){
            audioPlayerInteractor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists : List<Playlist>){
        if(playlists.isEmpty()){
            renderState(PlaylistsState.Empty("Вы не создали ни одного плейлиста"))
        }
        else{
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState){
        playListsState.postValue(state)
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