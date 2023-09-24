package com.example.myyandexproject.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myyandexproject.domain.settings.api.SettingsInteractor

class SettingsViewModel(private val sharedInteractor: SettingsInteractor) : ViewModel() {

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
    }

}