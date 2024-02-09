package com.example.myyandexproject.ui.player.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.ActivityAudioPlayerBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.Track
import com.example.myyandexproject.ui.PlaylistsState
import com.example.myyandexproject.ui.player.viewModel.AudioPlayerViewModel
import com.example.myyandexproject.utils.convertTime
import com.example.myyandexproject.utils.getBigImageUrl
import com.example.myyandexproject.utils.getYearFromReleaseDate
import com.example.myyandexproject.ui.player.PlayerState
import com.example.myyandexproject.ui.player.recycleView.PlaylistVerticalAdapter
import com.example.myyandexproject.ui.player.recycleView.PlaylistVerticalClick
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(
            track
        )
    }

    private lateinit var binding : ActivityAudioPlayerBinding
    private lateinit var track : Track
    private lateinit var bottomSheet : ConstraintLayout
    private val playlistAdapter = PlaylistVerticalAdapter()

    companion object {
        private const val TRACK_DATA = "track_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = getTrackFromBundle()
        setTrackData(track)

        bottomSheet = binding.bottomSheet

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.getPlayerState().observe(this){ playerState ->
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

        viewModel.getPlayListsState().observe(this){ playlistsState ->
            when(playlistsState){
                is PlaylistsState.Content -> {
                    playlistAdapter.playlists = playlistsState.playlists
                    playlistAdapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.isFavorite().observe(this) {
            changeFavoriteColor(it)
        }

        viewModel.getCurrentTrackTime().observe(this){ currentTime ->
            binding.currentDuration.text = currentTime
        }

        binding.playlists.layoutManager = LinearLayoutManager(this)
        binding.playlists.adapter = playlistAdapter

        playlistAdapter.setTrackClickListener(object : PlaylistVerticalClick {
            override fun onClick(playlist: Playlist) {
                if(playlist.idList.contains(track.trackId)){
                    Toast.makeText(this@AudioPlayerActivity, "Трек уже в плейлисте", Toast.LENGTH_SHORT).show()
                }
                else{
                    viewModel.addTrackInPlaylist(track, playlist)
                    Toast.makeText(this@AudioPlayerActivity, "Трек ${track.trackName} добавлен в плейлисте", Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        })


        binding.btnBack.setOnClickListener {
            super.onBackPressed()
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

        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun getTrackFromBundle() : Track {
        val bundle = intent.extras
        val trackData = bundle?.getString(TRACK_DATA)
        return Track.createTrackFromJson(trackData!!)
    }

    private fun changeFavoriteColor(isFavorite : Boolean){
        if(isFavorite){
            binding.favorite.setImageResource(R.drawable.baseline_favorite_24)
            binding.favorite.imageTintList = ColorStateList.valueOf(getColor(R.color.favorite_color))
        }
        else{
            binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)
            binding.favorite.imageTintList = ColorStateList.valueOf(getColor(R.color.white))
        }
    }

    private fun setTrackData(track: Track){
        binding.trackTitle.text = track.trackName
        binding.bandTitle.text = track.artistName
        binding.currentDuration.text = getString(R.string.initial_time)
        binding.albumValue.text = track?.collectionName
        binding.yearValue.text = getYearFromReleaseDate(track.releaseDate)
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
