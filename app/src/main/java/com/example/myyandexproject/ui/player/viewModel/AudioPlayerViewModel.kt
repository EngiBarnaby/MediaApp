package com.example.myyandexproject.ui.player.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.FavoritesRepository
import com.example.myyandexproject.domain.db.PlaylistTracksRepository
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.impl.PlaylistTrackInteractorImpl
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
    private val favoritesRepository: FavoritesRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistTracksRepository: PlaylistTracksRepository
) : ViewModel() {

    private var timerJob: Job? = null

    private var playerState = MutableLiveData(PlayerState.STATE_DEFAULT)
    private var currentTrackTime = MutableLiveData("00:00")
    private val isFavoriteData = MutableLiveData<Boolean>(false)
    private val playListsState = MutableLiveData<PlaylistsState>()

    fun getPlayerState() : LiveData<PlayerState> = playerState
    fun getCurrentTrackTime() : LiveData<String> = currentTrackTime
    fun isFavorite() : LiveData<Boolean> = isFavoriteData

    fun getPlayListsState() : LiveData<PlaylistsState> = playListsState

    init {
        isFavoriteData.postValue(track.isFavorite)
        preparePlayer()
        fetchPlayList()
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
            releaseDate=track.releaseDate,
            primaryGenreName=track.primaryGenreName,
            country=track.country,
            previewUrl=track.previewUrl
        )
        viewModelScope.launch {
            playlistTracksRepository.addTrackToPlaylist(playlistTrack)
        }

        viewModelScope.launch {
            playlistsRepository.addTrackToPlaylist(playlist.id!!, track.trackId)
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

    fun fetchPlayList(){

        viewModelScope.launch(Dispatchers.IO){
            playlistsRepository
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