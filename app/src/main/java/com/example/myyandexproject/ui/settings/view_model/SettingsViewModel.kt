package com.example.myyandexproject.ui.settings.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myyandexproject.creator.Creator
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.domain.settings.SettingsInteractorImp
import com.example.myyandexproject.domain.settings.api.SettingsInteractor
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.ui.player.view_model.AudioPlayerViewModel

class SettingsViewModel(context: Context, private val sharedInteractor: SettingsInteractor) : ViewModel() {

    private var themeState = MutableLiveData(false)

    fun isDarkThemeState() : LiveData<Boolean> = themeState
    fun setThemeState(value : Boolean){
        sharedInteractor.setThemeStatus(THEME_KEY, value)
        themeState.value = value
    }

    init {
        themeState.value = sharedInteractor.getThemeStatus(THEME_KEY)
    }

    companion object {
        private const val THEME_KEY = "THEME_KEY"

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    context,
                    Creator.getSettingsInteractor(context)
                )
            }
        }
    }

}