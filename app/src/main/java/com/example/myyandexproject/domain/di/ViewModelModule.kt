package com.example.myyandexproject.domain.di

import android.media.MediaPlayer
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.media.viewModels.CreatePlaylistViewModel
import com.example.myyandexproject.ui.media.viewModels.FavoriteViewModel
import com.example.myyandexproject.ui.media.viewModels.PlaylistViewModel
import com.example.myyandexproject.ui.player.viewModel.AudioPlayerViewModel
import com.example.myyandexproject.ui.playlist.viewModel.EditPlaylistViewModel
import com.example.myyandexproject.ui.playlist.viewModel.PlaylistDetailViewModel
import com.example.myyandexproject.ui.search.viewModel.SearchViewModel
import com.example.myyandexproject.ui.settings.viewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {(track : Track) ->
        AudioPlayerViewModel(track, MediaPlayer(), get())
    }

    viewModel {
        FavoriteViewModel(androidContext(), get())
    }

    viewModel {
        PlaylistViewModel(androidContext(), get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (playList : Playlist) ->
        PlaylistDetailViewModel(playList, get())
    }

    viewModel { (playlist : Playlist) ->
        EditPlaylistViewModel(playlist, get())
    }

}