package com.example.myyandexproject.ui.media.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyandexproject.domain.models.Playlist

class CreatePlaylistViewModel : ViewModel() {

    private val playlistData = MutableLiveData<Playlist>()

    fun getPlaylistData() : LiveData<Playlist> = playlistData

    init {
        Log.i("my_test", "It's fucking work!")
    }

}