package com.example.myyandexproject.ui.media.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.ActivityMediaBinding
import com.example.myyandexproject.databinding.ActivitySettingsBinding
import com.example.myyandexproject.ui.media.fragments.MediaLibraryAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaLibraryAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.table_name_favorite)
                1 -> tab.text = getString(R.string.table_name_playlist)
            }
        }
        tabMediator.attach()

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}