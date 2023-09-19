package com.example.myyandexproject.ui.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myyandexproject.R
import com.example.myyandexproject.ui.player.activity.AudioPlayer
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.data.dto.TrackResponse
import com.example.myyandexproject.data.network.ItunesApi
import com.example.myyandexproject.databinding.ActivityAudioPlayerBinding
import com.example.myyandexproject.databinding.ActivitySearchBinding
import com.example.myyandexproject.databinding.ActivitySettingsBinding
import com.example.myyandexproject.domain.search.api.TrackState
import com.example.myyandexproject.ui.search.recycle_view.TrackAdapter
import com.example.myyandexproject.ui.search.recycle_view.TrackClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.example.myyandexproject.retrofit_services.RetrofitItunesClient
import com.example.myyandexproject.ui.main.MainActivity
import com.example.myyandexproject.ui.search.view_model.SearchViewModel
import com.example.myyandexproject.ui.settings.view_model.SettingsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val INPUT_DEBOUNCE_DELAY = 1500L

        private const val TRACK_DATA = "track_data"
    }

    private lateinit var binding : ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel


    private val trackAdapter = TrackAdapter()
    private val historyTrackAdapter = TrackAdapter()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { makeRequest() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(this))[SearchViewModel::class.java]

        viewModel.getHistoryTracks().observe(this){ outTracks ->
            historyTrackAdapter.tracks = outTracks
            historyTrackAdapter.notifyDataSetChanged()
        }

        viewModel.getTextInput().observe(this){ text ->
            binding.searchInput.setText(text)
        }

        viewModel.getTrackState().observe(this){ state ->
            when(state){
                is TrackState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.trackList.visibility = View.GONE
                }
                is TrackState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.trackList.visibility = View.GONE
                    binding.errorRequestContainer.visibility = View.GONE
                    binding.notFoundContainer.visibility = View.VISIBLE
                }
                is TrackState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.trackList.visibility = View.GONE
                    binding.notFoundContainer.visibility = View.GONE
                    binding.errorRequestContainer.visibility = View.VISIBLE
                }
                is TrackState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    binding.notFoundContainer.visibility = View.GONE
                    binding.errorRequestContainer.visibility = View.GONE
                    binding.trackList.visibility = View.VISIBLE
                    trackAdapter.tracks = state.tracks
                    trackAdapter.notifyDataSetChanged()
                }
            }
        }

        historyTrackAdapter.setTrackClickListener( object : TrackClick {
            override fun onClick(track: Track) {
                viewModel.setHistoryTracks(track)
                startMediaActivity(track)
            }
        })

        trackAdapter.setTrackClickListener(object : TrackClick {
            override fun onClick(track: Track) {
                viewModel.setHistoryTracks(track)
                startMediaActivity(track)
            }
        })

        binding.historyTracks.layoutManager = LinearLayoutManager(this)
        binding.historyTracks.adapter = historyTrackAdapter

        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = trackAdapter

        binding.btnBack.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
            binding.songsHistory.visibility = View.GONE
            binding.emptyHistoryTitle.visibility = View.VISIBLE
        }

        binding.clearIcon.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            binding.searchInput.setText("")
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        binding.searchInput.setOnFocusChangeListener {view, isFocus ->
            if(historyTrackAdapter.tracks.isNotEmpty() && isFocus){
                binding.songsHistory.visibility = if (binding.searchInput.text.isEmpty()) View.VISIBLE else View.GONE
                binding.emptyHistoryTitle.visibility = View.GONE
            }
            else{
                binding.songsHistory.visibility = View.GONE
                binding.emptyHistoryTitle.visibility = if (binding.searchInput.text.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        val searchTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setInputText(s.toString())
                if (binding.searchInput.hasFocus() && s?.isEmpty() == true){
                    if(historyTrackAdapter.tracks.isNotEmpty()){
                        binding.songsHistory.visibility = View.VISIBLE
                        binding.emptyHistoryTitle.visibility = View.GONE
                    }
                    else{
                        binding.songsHistory.visibility = View.GONE
                        binding.emptyHistoryTitle.visibility = View.VISIBLE
                    }
                }
                else{
                    binding.songsHistory.visibility = View.GONE
                    binding.emptyHistoryTitle.visibility = View.GONE
                }
                if (s.isNullOrEmpty()) {
                    binding.searchInput.hint = getString(R.string.search)
                } else {
                    binding.searchInput.hint = ""
                    searchDebounce()
                }
                binding.clearIcon.visibility = clearIconIsVisible(s)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Empty
            }
        }

        binding.searchInput.addTextChangedListener(searchTextInputWatcher)

        binding.refreshBtn.setOnClickListener {
            makeRequest()
        }

    }

    private fun startMediaActivity(track : Track){
        if (clickDebounce()) {
            val audioPlayerIntent = Intent(this, AudioPlayer::class.java)
            val trackData = Track.createJsonFromTrack(track)
            audioPlayerIntent.putExtra(TRACK_DATA, trackData)
            startActivity(audioPlayerIntent)
        }
    }

    private fun makeRequest(){
        viewModel.makeRequest()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, INPUT_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clearIconIsVisible(s : CharSequence?) : Int {
        return if(s.isNullOrEmpty()){
            View.GONE
        }
        else{
            View.VISIBLE
        }
    }

}
