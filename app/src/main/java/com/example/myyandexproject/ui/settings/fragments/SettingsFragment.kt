package com.example.myyandexproject.ui.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentMainMediaBinding
import com.example.myyandexproject.databinding.FragmentSettingsBinding
import com.example.myyandexproject.ui.media.fragments.MainMediaFragment
import com.example.myyandexproject.ui.media.fragments.MediaLibraryAdapter
import com.example.myyandexproject.ui.settings.view_model.SettingsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun getInstance() = MainMediaFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isDarkThemeState().observe(viewLifecycleOwner){ themeState ->
            binding.settingThemeSwitcher.isChecked = themeState
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}