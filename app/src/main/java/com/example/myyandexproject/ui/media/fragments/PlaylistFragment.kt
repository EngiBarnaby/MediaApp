package com.example.myyandexproject.ui.media.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentPlaylistBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.ui.PlaylistsState
import com.example.myyandexproject.ui.media.recycleView.favorites.PlaylistAdapter
import com.example.myyandexproject.ui.media.recycleView.favorites.PlaylistClick
import com.example.myyandexproject.ui.media.viewModels.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private val playlistAdapter = PlaylistAdapter()

    companion object {
        fun getInstance(isCreated: Boolean? = null): PlaylistFragment {
            val fragment = PlaylistFragment()
            fragment.arguments = createArgs(isCreated)
            return fragment
        }

        private const val ARGS_IS_CREATED = "created"

        private fun createArgs(isCreated: Boolean?): Bundle =
            bundleOf(ARGS_IS_CREATED to isCreated)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlayList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlayListsState().observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistsState.Empty -> {
                    showEmpty()
                }
                is PlaylistsState.Loading -> {
                    showLoading()
                }
                is PlaylistsState.Content -> {
                    playlistAdapter.playlists = state.playlists
                    playlistAdapter.notifyDataSetChanged()
                    showContent()
                }
            }
        }

        val isPlaylistCreated = requireArguments().getBoolean(ARGS_IS_CREATED, false)

        if(isPlaylistCreated){
            Toast.makeText(requireContext(),
                requireContext().getString(R.string.new_playlist_has_been_created), Toast.LENGTH_SHORT).show()
        }

        binding.newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainMediaFragment_to_createPlaylistFragment)
        }

        playlistAdapter.setTrackClickListener(object : PlaylistClick {
            override fun onClick(playlist: Playlist) {
                Log.i("test", "Click to playlist is work")
            }
        })

        binding.playlists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlists.adapter = playlistAdapter

    }

    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.emptyPlaylist.visibility = View.GONE
        binding.playlists.visibility = View.GONE
    }

    private fun showContent(){
        binding.progressBar.visibility = View.GONE
        binding.emptyPlaylist.visibility = View.GONE
        binding.playlists.visibility = View.VISIBLE
    }

    private fun showEmpty(){
        binding.progressBar.visibility = View.GONE
        binding.emptyPlaylist.visibility = View.VISIBLE
        binding.playlists.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}