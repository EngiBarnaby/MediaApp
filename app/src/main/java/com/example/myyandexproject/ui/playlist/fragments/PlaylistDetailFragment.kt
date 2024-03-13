package com.example.myyandexproject.ui.playlist.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentPlaylistDetailBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.domain.models.PlaylistTrack
import com.example.myyandexproject.ui.playlist.recycleView.PlaylistTrackAdapter
import com.example.myyandexproject.ui.playlist.recycleView.PlaylistTrackClick
import com.example.myyandexproject.ui.playlist.recycleView.PlaylistTrackLongClick
import com.example.myyandexproject.ui.playlist.viewModel.PlaylistDetailViewModel
import com.example.myyandexproject.ui.test.PlaylistTracksState
import com.example.myyandexproject.utils.convertTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : Fragment() {

    private val viewModel : PlaylistDetailViewModel by viewModel {
        parametersOf(
            playlist
        )
    }

    private var _binding : FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private val playlistAdapter = PlaylistTrackAdapter()
    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var bottomSheetShare : ConstraintLayout
    private lateinit var bottomSheetMenu : ConstraintLayout
    private lateinit var playlist: Playlist


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = getPlaylistFromBundle()

        viewModel.getPlaylistTracksState().observe(viewLifecycleOwner){ playlistTrackState ->
            when(playlistTrackState){
                is PlaylistTracksState.Content -> {
                    playlistAdapter.playlistTracks = playlistTrackState.playlistTracks
                    playlistAdapter.notifyDataSetChanged()
                    renderPlaylistDetail(playlist)
                    showTracks()
                }
                is PlaylistTracksState.Empty -> {
                    showEmpty()
                }
            }
        }

        viewModel.getPlaylistDetails().observe(viewLifecycleOwner){ playlist ->
            renderPlaylistDetail(playlist)
        }


        bottomSheet = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.peek_for_bottom_sheet)

        bottomSheetShare = binding.bottomSheetShare
        val bottomSheetBehaviorShare = BottomSheetBehavior.from(bottomSheetShare)
        bottomSheetBehaviorShare.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetMenu = binding.bottomSheetPlaylistMenu
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetMenu)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        setBottomSheetMenu()

        playlistAdapter.setTrackClickListener(object : PlaylistTrackClick {
            override fun onClick(playlistTrack: PlaylistTrack) {
                val trackData = PlaylistTrack.createJsonFromTrack(playlistTrack)
                val bundle = Bundle()
                bundle.putString(TRACK_DATA, trackData)
                findNavController().navigate(R.id.action_playlistDetailFragment_to_audioPlayerFragment2, bundle)
            }
        })
        
        playlistAdapter.setTrackLongClick(object : PlaylistTrackLongClick {
            override fun onLongClick(playlistTrack: PlaylistTrack) {
                showModal(playlistTrack)
            }
          }
        )

        binding.shareIcon.setOnClickListener {
            if (!playlistAdapter.playlistTracks.isEmpty()){
                openShareClient()
            }
            else{
                bottomSheetBehaviorShare.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        binding.shareBottomSheet.setOnClickListener {
            if (!playlistAdapter.playlistTracks.isEmpty()){
                openShareClient()
            }
            else{
                bottomSheetBehaviorShare.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        binding.deletePlaylistBottomSheet.setOnClickListener {
            showDeletePlaylistModal()
        }

        binding.playlistTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistTracks.adapter = playlistAdapter

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.menuIcon.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.editInfoBottomSheet.setOnClickListener {
            val playlistData = Playlist.createJsonFromPlaylist(playlist)
            val bundle = Bundle()
            bundle.putString(PLAYLIST_DATA, playlistData)
            findNavController().navigate(R.id.action_playlistDetailFragment_to_editPlaylistFragment, bundle)
        }

        binding.shareBottomSheet.setOnClickListener {
            if (!playlistAdapter.playlistTracks.isEmpty()){
                openShareClient()
            }
            else{
                bottomSheetBehaviorShare.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchPlaylistDetails()
    }

    private fun renderPlaylistDetail(playlist: Playlist){
        binding.apply {
            Glide.with(this@PlaylistDetailFragment)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.track_image_placeholder)
                .override(360, 360)
                .centerCrop()
                .into(playlistImage)
            playlistTitle.text = playlist.title
            description.text = playlist.description
            tracksCount.text = viewModel.getPlaylistTracksCount()
            playlistDuration.text = viewModel.getPlaylistDuration()
        }
    }

    companion object {
        private const val PLAYLIST_DATA = "playlist_data"
        private const val TRACK_DATA = "track_data"
    }

    private fun showDeletePlaylistModal(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить плейлист ${playlist.title}?")
            .setNegativeButton("Нет"){ dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Да") { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun showModal(playlistTrack: PlaylistTrack){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Удалить") { dialog, which ->
                viewModel.removeTrackFromPlaylist(track = playlistTrack)
                dialog.cancel()
            }
            .show()
    }

    private fun setBottomSheetMenu(){
        binding.apply {
            Glide.with(this@PlaylistDetailFragment)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.track_image_placeholder)
                .override(45, 45)
                .centerCrop()
                .into(playlistImageBottomSheet)
            playlistTitleBottomSheet.text = playlist.title
            playlistTrackCountBottomSheet.text = viewModel.getPlaylistTracksCount()
        }
    }

    private fun openShareClient(){
        var shareText = "${playlist.title}\n${playlist.description}\n${viewModel.getPlaylistTracksCount()}\n"
        playlistAdapter.playlistTracks.forEach {
            shareText += "${it.trackName}. ${it.artistName}. ${convertTime(it.trackTimeMillis.toInt())}\n"
        }
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_text)))
    }

    private fun showTracks(){
        binding.playlistTracks.visibility = View.VISIBLE
        binding.emptyContainer.visibility = View.GONE
    }

    private fun showEmpty(){
        binding.playlistTracks.visibility = View.GONE
        binding.emptyContainer.visibility = View.VISIBLE
    }

    private fun getPlaylistFromBundle() : Playlist {
        val bundle = arguments
        val playlist_data = bundle?.getString(PLAYLIST_DATA)
        return Playlist.createPlaylistFromJson(playlist_data!!)
    }

}