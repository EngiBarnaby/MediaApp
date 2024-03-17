package com.example.myyandexproject.ui.media.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FavoriteFragmentsBinding
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.FavoritesState
import com.example.myyandexproject.ui.media.recycleView.favorites.FavoriteAdapter
import com.example.myyandexproject.ui.media.viewModels.FavoriteViewModel
import com.example.myyandexproject.ui.search.recycleView.TrackClick
import com.example.myyandexproject.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private var _binding: FavoriteFragmentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModel()
    private val favoriteAdapter = FavoriteAdapter()
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FavoriteFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            startMediaActivity(track)
        }

        viewModel.getFavoriteState().observe(viewLifecycleOwner){ state ->
            when(state){
                is FavoritesState.Loading -> {
                    showLoading()
                }
                is FavoritesState.Empty -> {
                    showEmpty()
                }
                is FavoritesState.Content -> {
                    showContent()
                    favoriteAdapter.tracks = state.tracks
                    favoriteAdapter.notifyDataSetChanged()
                }
            }
        }

        favoriteAdapter.setTrackClickListener(object : TrackClick {
            override fun onClick(track: Track) {
                val isFavoriteTrack = track.copy(isFavorite = true)
                onTrackClickDebounce(isFavoriteTrack)
            }

        })

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = favoriteAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesTrack()
    }

    private fun startMediaActivity(track : Track){
        val trackData = Track.createJsonFromTrack(track)
        val bundle = Bundle()
        bundle.putString(TRACK_DATA, trackData)
        findNavController().navigate(R.id.action_mainMediaFragment_to_audioPlayerFragment2, bundle)
    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyFavoritePlaceholder.visibility = View.GONE
        binding.trackList.visibility = View.GONE
    }

    private fun showContent(){
        binding.progressBar.visibility = View.GONE
        binding.emptyFavoritePlaceholder.visibility = View.GONE
        binding.trackList.visibility = View.VISIBLE
    }

    private fun showEmpty(){
        binding.progressBar.visibility = View.GONE
        binding.emptyFavoritePlaceholder.visibility = View.VISIBLE
        binding.trackList.visibility = View.GONE
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val INPUT_DEBOUNCE_DELAY = 1500L

        private const val TRACK_DATA = "track_data"
        fun getInstance() = FavoriteFragment()
    }

}