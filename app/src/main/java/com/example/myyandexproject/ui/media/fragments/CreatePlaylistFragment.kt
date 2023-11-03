package com.example.myyandexproject.ui.media.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentCreatePlaylistBinding
import com.example.myyandexproject.ui.media.viewModels.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylistData().observe(viewLifecycleOwner){ playlistData ->
            if(playlistData.title.isNullOrBlank()){
                changeCreateBtnState(false)
            }
            else{
                changeCreateBtnState(true)
            }
        }

    }

    fun changeCreateBtnState(isEmpty : Boolean){
        if(isEmpty){
            val disableColor = ContextCompat.getColor(requireContext(), R.color.disable_btn_color)
            binding.createPlaylistBtn.setBackgroundColor(disableColor)
        }
        else{
            val activeColor = ContextCompat.getColor(requireContext(), R.color.active_btn_color)
            binding.createPlaylistBtn.setBackgroundColor(activeColor)
        }
    }

    companion object {
        fun getInstance() = CreatePlaylistFragment()
    }


}