package com.example.myyandexproject

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.myyandexproject.data.di.dataModule
import com.example.myyandexproject.domain.di.interactorModule
import com.example.myyandexproject.domain.di.repositoryModule
import com.example.myyandexproject.domain.di.viewModelModule
import com.example.myyandexproject.domain.settings.api.SettingsInteractor
import com.example.myyandexproject.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    private val interactor: SettingsInteractor by inject()

    companion object {
        private const val THEME_KEY = "THEME_KEY"
    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        checkTheme()
    }

    private fun checkTheme(){
        val themeState = interactor.getThemeStatus(THEME_KEY)
        if (themeState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}