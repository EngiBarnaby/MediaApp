package com.example.myyandexproject.ui.media.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.ui.FavoritesState
import com.example.myyandexproject.ui.PlaylistsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsRepository: PlaylistsRepository) : ViewModel() {

    private val playListsState = MutableLiveData<PlaylistsState>()

    fun getPlayListsState() : LiveData<PlaylistsState> = playListsState

    fun fetchPlayList(){

        viewModelScope.launch(Dispatchers.IO){
            playlistsRepository
                .getPlaylists()
                .collect { playlists ->
                    Log.i("playlists", playlists.toString())
                    processResult(playlists)
                }
        }
    }

    init {
        fetchPlayList()
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

}