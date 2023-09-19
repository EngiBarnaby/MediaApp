package com.example.myyandexproject.data.shared_preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) : SharedPreferencesClient {

    companion object {
        private val SHARED_PREFERENCES_NAME = "music_app_shared_preferences_key"
    }


    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun setBooleanValue(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getStringValue(key: String): String {
        val data = sharedPreferences.getString(key, null)
        return data ?: ""
    }

    override fun setStringValue(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

}