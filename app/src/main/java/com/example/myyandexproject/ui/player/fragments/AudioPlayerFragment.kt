package com.example.myyandexproject.ui.player.fragments

import android.content.res.ColorStateList
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentAudioPlayerBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.PlaylistsState
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.ui.player.recycleView.PlaylistVerticalAdapter
import com.example.myyandexproject.ui.player.recycleView.PlaylistVerticalClick
import com.example.myyandexproject.ui.player.viewModel.AudioPlayerViewModel
import com.example.myyandexproject.utils.convertTime
import com.example.myyandexproject.utils.getBigImageUrl
import com.example.myyandexproject.utils.getYearFromReleaseDate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(
            track
        )
    }

    private var _binding : FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track : Track
    private lateinit var bottomSheet : ConstraintLayout
    private val playlistAdapter = PlaylistVerticalAdapter()

    private lateinit var bottomSheetCallback: BottomSheetCallback

    companion object {
        private const val TRACK_DATA = "track_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = getTrackFromBundle()
        setTrackData(track)

        bottomSheet = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetCallback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.bottomSheetBackground.visibility = View.GONE
                    }
                    else -> {
                        binding.bottomSheetBackground.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        }

        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        viewModel.getPlayerState().observe(viewLifecycleOwner){ playerState ->
            when(playerState){
                PlayerState.STATE_DEFAULT, PlayerState.STATE_PAUSED -> {
                    binding.playerBtn.setIconResource(R.drawable.baseline_play_arrow_24)
                }
                PlayerState.STATE_PLAYING -> {
                    binding.playerBtn.setIconResource(R.drawable.baseline_pause_24)
                }
                PlayerState.STATE_PREPARED -> {
                    binding.playerBtn.setIconResource(R.drawable.baseline_play_arrow_24)
                    viewModel.preparedPlayer()
                }
            }
        }

        viewModel.getPlayListsState().observe(viewLifecycleOwner){ playlistsState ->
            when(playlistsState){
                is PlaylistsState.Content -> {
                    playlistAdapter.playlists = playlistsState.playlists
                    playlistAdapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.isFavorite().observe(viewLifecycleOwner) {
            changeFavoriteColor(it)
        }

        viewModel.getCurrentTrackTime().observe(viewLifecycleOwner){ currentTime ->
            binding.currentDuration.text = currentTime
        }

        binding.playlists.layoutManager = LinearLayoutManager(requireContext())
        binding.playlists.adapter = playlistAdapter

        playlistAdapter.setTrackClickListener(object : PlaylistVerticalClick {
            override fun onClick(playlist: Playlist) {
                if(playlist.idList.contains(track.trackId)){
                    Toast.makeText(requireContext(), "${requireContext().getString(R.string.track_already_in_playlist)} ${playlist.title}", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.addTrackInPlaylist(track, playlist)
                    Toast.makeText(requireContext(), "${requireContext().getString(R.string.track_added_to_playlist)} ${playlist.title}", Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        })


        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playerBtn.setOnClickListener {
            when(viewModel.getPlayerState().value){
                PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                    viewModel.startPlayer()
                }
                PlayerState.STATE_PLAYING -> {
                    viewModel.pausePlayer()
                }
            }
        }

        binding.favorite.setOnClickListener {
            viewModel.changeFavoriteStatus()
        }

        binding.playlist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun getTrackFromBundle() : Track {
        val bundle = arguments
        val trackData = bundle?.getString(TRACK_DATA)
        return Track.createTrackFromJson(trackData!!)
    }

    private fun changeFavoriteColor(isFavorite : Boolean){
        if(isFavorite){
            binding.favorite.setImageResource(R.drawable.baseline_favorite_24)
            binding.favorite.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.favorite_color))
        }
        else{
            binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)
            binding.favorite.imageTintList = ColorStateList.valueOf(requireContext().getColor(R.color.white))
        }
    }

    private fun setTrackData(track: Track){
        binding.trackTitle.text = track.trackName
        binding.bandTitle.text = track.artistName
        binding.currentDuration.text = getString(R.string.initial_time)
        binding.albumValue.text = track?.collectionName
        binding.yearValue.text = getYearFromReleaseDate(track.releaseDate ?: "Нет")
        binding.genreValue.text = track.primaryGenreName
        binding.country.text = track.country

        binding.durationValue.text = convertTime(track.trackTimeMillis?.toInt())

        changeFavoriteColor(track.isFavorite)

        Glide.with(binding.imageView)
            .load(getBigImageUrl(track.artworkUrl100))
            .placeholder(R.drawable.track_image_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.round_image))))
            .into(binding.imageView)
    }

}