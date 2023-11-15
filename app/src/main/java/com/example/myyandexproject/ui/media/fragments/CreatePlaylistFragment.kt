package com.example.myyandexproject.ui.media.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
                changeCreateBtnState(true)
            }
            else{
                changeCreateBtnState(false)
            }
        }

        changeCreateBtnState(isEmpty = true)

        viewModel.getTitle().observe(viewLifecycleOwner){
            if(it.isEmpty()){
                changeCreateBtnState(isEmpty = true)
            }
            else{
                changeCreateBtnState(isEmpty = false)
            }
        }

        val titleTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setTitle(s.toString())
            }
        }

        val descriptionTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s.toString())
            }
        }

        binding.playlistTitle.addTextChangedListener(titleTextInputWatcher)
        binding.playlistTitle.addTextChangedListener(descriptionTextInputWatcher)

        binding.createPlaylistBtn.setOnClickListener {
//            viewModel.createPlaylist()
            Toast.makeText(requireContext(), "This is work!!!!", Toast.LENGTH_SHORT).show()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imagePicker.visibility = View.GONE
                    binding.userImage.setImageURI(uri)
                    binding.userImage.visibility = View.VISIBLE
                    viewModel.setImageUrl(uri.toString())
                } else {
                    Toast.makeText(requireContext(), "Изображение не было выбрано", Toast.LENGTH_SHORT).show()
                }
            }

        binding.imagePicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }

    fun changeCreateBtnState(isEmpty : Boolean){
        if(isEmpty){
            val disableColor = ContextCompat.getColor(requireContext(), R.color.disable_btn_color)
            binding.createPlaylistBtn.let {
                it.setBackgroundColor(disableColor)
                it.isEnabled = false
                it.isClickable = false
            }
        }
        else{
            val activeColor = ContextCompat.getColor(requireContext(), R.color.active_btn_color)
            binding.createPlaylistBtn.let {
                it.setBackgroundColor(activeColor)
                it.isEnabled = true
                it.isClickable = true
            }
        }
    }

    companion object {
        fun getInstance() = CreatePlaylistFragment()
    }


}