package com.example.myyandexproject.ui.playlist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyandexproject.domain.db.EditPlaylistInteractor
import com.example.myyandexproject.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlist: Playlist,
    private val editPlaylistInteractor: EditPlaylistInteractor
) : ViewModel() {

    fun updatePlaylist(updatedPlaylist: Playlist) {
        viewModelScope.launch {
            editPlaylistInteractor.updatePlaylist(updatedPlaylist)
        }
    }

}