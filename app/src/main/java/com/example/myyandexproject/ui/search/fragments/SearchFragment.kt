package com.example.myyandexproject.ui.search.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentSearchBinding
import com.example.myyandexproject.domain.search.api.TrackState
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.search.recycleView.TrackAdapter
import com.example.myyandexproject.ui.search.recycleView.TrackClick
import com.example.myyandexproject.ui.search.viewModel.SearchViewModel
import com.example.myyandexproject.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val INPUT_DEBOUNCE_DELAY = 1500L

        private const val TRACK_DATA = "track_data"
    }

    private var latestSearchText: String? = null
    private var searchJob: Job? = null


    private val trackAdapter = TrackAdapter()
    private val historyTrackAdapter = TrackAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.getInputText().isNotBlank()){
            viewModel.makeRequest()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            viewModel.setHistoryTracks(track)
            startMediaActivity(track)
        }

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
                onTrackClickDebounce(track)
            }
        })

        trackAdapter.setTrackClickListener(object : TrackClick {
            override fun onClick(track: Track) {
                  onTrackClickDebounce(track)
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
                    searchDebounce(s.toString())
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
        val trackData = Track.createJsonFromTrack(track)
        val bundle = Bundle()
        bundle.putString(TRACK_DATA, trackData)
        val navController = findNavController()
        navController.navigate(R.id.action_searchFragment_to_audioPlayerFragment, bundle)
    }

    private fun makeRequest(){
        viewModel.makeRequest()
    }

    private fun searchDebounce(inputText : String) {
        if (latestSearchText == inputText) {
            return
        }

        latestSearchText = inputText

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(INPUT_DEBOUNCE_DELAY)
            makeRequest()
        }
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