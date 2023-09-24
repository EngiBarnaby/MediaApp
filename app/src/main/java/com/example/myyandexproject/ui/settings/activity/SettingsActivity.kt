package com.example.myyandexproject.ui.settings.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.ActivitySettingsBinding
import com.example.myyandexproject.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.isDarkThemeState().observe(this){ themeState ->
            binding.settingThemeSwitcher.isChecked = themeState
        }


        binding.btnBack.setOnClickListener {
            super.onBackPressed();
        }

        binding.btnWriteSupport.setOnClickListener {
            openEmailClient()
        }

        binding.btnWebPage.setOnClickListener {
            openWebClient()
        }

        binding.btnShare.setOnClickListener {
            openShareClient()
        }

        binding.settingThemeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            viewModel.setThemeState(isChecked)
        }
    }


    private fun openShareClient(){
        val url = getString(R.string.share_url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_text)))
    }

    private fun openWebClient(){
        val url = getString(R.string.practicum_offer_url)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun openEmailClient() {
        val recipientEmail = getString(R.string.email_user)
        val emailSubject = getString(R.string.email_subject)
        val emailBody = getString(R.string.email_body)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailBody)

        startActivity(Intent.createChooser(intent, getString(R.string.email_info_text)))
    }

}