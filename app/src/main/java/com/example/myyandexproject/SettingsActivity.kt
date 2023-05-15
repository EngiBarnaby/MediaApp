package com.example.myyandexproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitcher : SwitchMaterial

    companion object {
        const val THEME_SWITCH_STATE = "THEME_SWITCH_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<TextView>(R.id.btnBack)
        val btnShare = findViewById<FrameLayout>(R.id.btn_share)
        val btnWriteSupport = findViewById<FrameLayout>(R.id.btn_write_support)
        val btnWebPage = findViewById<FrameLayout>(R.id.btn_web_page)
        themeSwitcher = findViewById(R.id.setting_theme_switcher)


        if (savedInstanceState != null) {
            val switchState = savedInstanceState.getBoolean(THEME_SWITCH_STATE,false)
            themeSwitcher.isChecked = switchState
        }


        btnBack.setOnClickListener {
            super.onBackPressed();
        }

        btnWriteSupport.setOnClickListener {
            openEmailClient()
        }

        btnWebPage.setOnClickListener {
            openWebClient()
        }

        btnShare.setOnClickListener {
            openShareClient()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(THEME_SWITCH_STATE, themeSwitcher.isChecked)
    }

}