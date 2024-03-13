package com.example.myyandexproject.ui.playlist.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.R
import com.example.myyandexproject.domain.db.PlaylistDetailInteractor
import com.example.myyandexproject.domain.db.PlaylistInteractor
import com.example.myyandexproject.domain.db.PlaylistTrackInteractor
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.ui.FavoritesState
import com.example.myyandexproject.ui.PlaylistsState
import com.example.myyandexproject.ui.test.PlaylistTracksState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailViewModel(
    private var playlist: Playlist,
    private var playlistDetailInteractor: PlaylistDetailInteractor
) : ViewModel() {

    private val playlistTracksState = MutableLiveData<PlaylistTracksState>()
    private val playlistDetail = MutableLiveData<Playlist>()

    fun getPlaylistTracksState() : LiveData<PlaylistTracksState> = playlistTracksState
    fun getPlaylistDetails() : LiveData<Playlist> = playlistDetail

    fun getPlaylistTracksCount() : String {
        if(playlistTracksState.value is PlaylistTracksState.Content){
            val tracksCount = (playlistTracksState.value as PlaylistTracksState.Content).playlistTracks.count()
            return when(tracksCount){
                0 -> "$tracksCount треков"
                1 -> "$tracksCount трек"
                in 2..4 -> "$tracksCount трека"
                else -> "$tracksCount треков"
            }
        }
        else {
            return "0 треков"
        }
    }

    fun getPlaylistDuration() : String {
        if(playlistTracksState.value is PlaylistTracksState.Content){
            val millis = (playlistTracksState.value as PlaylistTracksState.Content).playlistTracks.sumOf { playlistTrack -> playlistTrack.trackTimeMillis.toInt() }
            val durationTracks = SimpleDateFormat("mm", Locale.getDefault()).format(millis).toInt()
            return when(durationTracks){
                0 -> "${durationTracks} минут"
                1 -> "${durationTracks} минута"
                in 2..4 -> "${durationTracks} минуты"
                else -> "${durationTracks} минут"
            }
        }
        else {
            return "0 минут"
        }
    }

    init {
        fetchPlaylistTracks()
        fetchPlaylistDetails()
    }

    fun removeTrackFromPlaylist(track: PlaylistTrack){
        val newTracksIds = playlist.idList.toMutableList()
        newTracksIds.remove(track.trackId)
        playlist.idList = newTracksIds
        viewModelScope.launch {
            val job = async { playlistDetailInteractor.removeTrackFromPlaylist(playlistId = playlist.id!!, playlistTrackIds = playlist.idList) }
            job.await()
            playlistDetailInteractor.checkPlaylistTrackExistence(track)
            fetchPlaylistTracks()
        }
    }

    fun deletePlaylist(){
        viewModelScope.launch {
            playlistDetailInteractor.deletePlaylist(playlist)
        }
    }

    fun fetchPlaylistDetails(){
        viewModelScope.launch {
            playlistDetailInteractor
                .getPlaylistDetails(playlistId = playlist.id!!)
                .collect{playlist ->
                    playlistDetail.postValue(playlist)
                }
        }
    }

    fun fetchPlaylistTracks(){
        playlistTracksState.value = PlaylistTracksState.Loading
        viewModelScope.launch {
            playlistDetailInteractor.getPlaylistTracksByIds(playlist.idList)
                .collect {playlistTracksData ->
                    processResult(playlistTracksData)
                }
        }
    }

    private fun processResult(playlistTracks : List<PlaylistTrack>){
        if(playlistTracks.isEmpty()){
            renderState(PlaylistTracksState.Empty("В плейлисте нет треков"))
        }
        else{
            renderState(PlaylistTracksState.Content(playlistTracks))
        }
    }

    private fun renderState(state: PlaylistTracksState){
        playlistTracksState.postValue(state)
    }


}