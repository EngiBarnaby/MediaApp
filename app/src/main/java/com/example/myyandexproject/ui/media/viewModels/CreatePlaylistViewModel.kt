package com.example.myyandexproject.ui.media.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.PlaylistsRepository
import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class CreatePlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val playlistData = MutableLiveData<Playlist>()
    private val title = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val imageUrl = MutableLiveData<String>()

    fun getTitle() : LiveData<String> = title
    fun getDescription(): LiveData<String> = description
    fun getImageUrl(): LiveData<String> = imageUrl

    fun setTitle(value : String){
        title.postValue(value)
    }

    fun setDescription(value : String){
        description.postValue(value)
    }

    fun setImageUrl(value : String){
        imageUrl.postValue(value)
    }

    fun createPlaylist(){
        val playlist = Playlist(
            id = null,
            title = title.value!!,
            description = description.value,
            imageUrl = imageUrl.value,
            tracksCount = 0,
            idList = emptyList<Int>()
        )

        viewModelScope.launch(Dispatchers.IO){
            playlistsRepository.addPlaylist(playlist)
        }

    }

    fun getPlaylistData() : LiveData<Playlist> = playlistData

    init {

    }

}