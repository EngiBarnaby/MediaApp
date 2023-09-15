package com.example.myyandexproject.domain.settings.api

interface SettingsInteractor {
    fun getThemeStatus(key : String) : Boolean
    fun setThemeStatus(key: String, value : Boolean)
}