package com.example.myyandexproject.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.ActivityAudioPlayerBinding
import com.example.myyandexproject.domain.search.models.Track
import com.example.myyandexproject.ui.player.view_model.AudioPlayerViewModel
import com.example.myyandexproject.utils.convertTime
import com.example.myyandexproject.utils.getBigImageUrl
import com.example.myyandexproject.utils.getYearFromReleaseDate
import com.example.myyandexproject.ui.player.PlayerState

class AudioPlayer : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel

    private lateinit var binding : ActivityAudioPlayerBinding
    private lateinit var track : Track

    companion object {
        private const val TRACK_DATA = "track_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrackFromBundle()
        setTrackData(track)

        viewModel = ViewModelProvider(this, AudioPlayerViewModel.getViewModelFactory(track))[AudioPlayerViewModel::class.java]

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
                    binding.currentDuration.text = getString(R.string.initial_time)
                }
            }
        }

        viewModel.getCurrentTrackTime().observe(this){ currentTime ->
            binding.currentDuration.text = currentTime
        }

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

    }

    private fun getTrackFromBundle() : Track {
        val bundle = intent.extras
        val trackData = bundle?.getString(TRACK_DATA)
        return Track.createTrackFromJson(trackData!!)
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

        Glide.with(binding.imageView)
            .load(getBigImageUrl(track.artworkUrl100))
            .placeholder(R.drawable.track_image_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.round_image))))
            .into(binding.imageView)
    }
}
