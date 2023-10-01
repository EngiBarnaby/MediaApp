package com.example.myyandexproject.domain.di

import android.media.MediaPlayer
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.ui.media.view_models.FavoriteViewModel
import com.example.myyandexproject.ui.media.view_models.PlaylistViewModel
import com.example.myyandexproject.ui.player.view_model.AudioPlayerViewModel
import com.example.myyandexproject.ui.search.view_model.SearchViewModel
import com.example.myyandexproject.ui.settings.view_model.SettingsViewModel
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
        AudioPlayerViewModel(track, MediaPlayer())
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }

}