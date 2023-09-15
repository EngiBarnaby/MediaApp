package com.example.myyandexproject.domain.settings

import com.example.myyandexproject.data.shared_preferences.SharedPreferencesClient
import com.example.myyandexproject.data.shared_preferences.SharedPreferencesManager
import com.example.myyandexproject.domain.settings.api.SettingsInteractor

class SettingsInteractorImp(private val sharedManager: SharedPreferencesClient) :
    SettingsInteractor {
    override fun getThemeStatus(key: String): Boolean {
        return sharedManager.getBooleanValue(key)
    }

    override fun setThemeStatus(key: String, value: Boolean) {
        sharedManager.setBooleanValue(key, value)
    }
}