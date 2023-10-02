package com.example.myyandexproject.ui.search.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentSearchBinding
import com.example.myyandexproject.domain.search.api.TrackState
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.ui.player.activity.AudioPlayer
import com.example.myyandexproject.ui.search.recycle_view.TrackAdapter
import com.example.myyandexproject.ui.search.recycle_view.TrackClick
import com.example.myyandexproject.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val INPUT_DEBOUNCE_DELAY = 1500L

        private const val TRACK_DATA = "track_data"
    }


    private val trackAdapter = TrackAdapter()
    private val historyTrackAdapter = TrackAdapter()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { makeRequest() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHistoryTracks().observe(viewLifecycleOwner){ outTracks ->
            historyTrackAdapter.tracks = outTracks
            historyTrackAdapter.notifyDataSetChanged()
        }

        viewModel.getTextInput().observe(viewLifecycleOwner){ text ->
            binding.searchInput.setText(text)
        }

        viewModel.getTrackState().observe(viewLifecycleOwner){ state ->
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

        binding.historyTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.historyTracks.adapter = historyTrackAdapter

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = trackAdapter


        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
            binding.songsHistory.visibility = View.GONE
            binding.emptyHistoryTitle.visibility = View.VISIBLE
        }

        binding.clearIcon.setOnClickListener {
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusView = requireActivity()?.currentFocus
            if (currentFocusView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
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
            val audioPlayerIntent = Intent(requireContext(), AudioPlayer::class.java)
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
        handler.postDelayed(searchRunnable, SearchFragment.INPUT_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, SearchFragment.CLICK_DEBOUNCE_DELAY)
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