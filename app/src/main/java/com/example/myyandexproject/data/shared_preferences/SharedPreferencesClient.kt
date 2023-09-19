package com.example.myyandexproject.data.shared_preferences

interface SharedPreferencesClient {

    fun setBooleanValue(key : String, value : Boolean)

    fun getBooleanValue(key : String) : Boolean

    fun getStringValue(key : String) : String

    fun setStringValue(key : String, value : String)
}