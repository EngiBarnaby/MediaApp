package com.example.myyandexproject.ui.playlist.fragments

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentEditPlaylistBinding
import com.example.myyandexproject.domain.models.Playlist
import com.example.myyandexproject.ui.playlist.viewModel.EditPlaylistViewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : Fragment() {

    private val viewModel : EditPlaylistViewModel by viewModel {
        parametersOf(
            playlist
        )
    }

    private var _binding : FragmentEditPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlist: Playlist
    private lateinit var newUrl : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = getPlaylistFromBundle()
        newUrl = playlist.imageUrl.toString()
        setDetailsInfo()
        setEnableBtn(true)

        val titleTextInputWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true){
                    setEnableBtn(false)
                }
                else{
                    setEnableBtn(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.playlistTitle.addTextChangedListener(titleTextInputWatcher)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imagePicker.visibility = View.GONE
                    binding.playlistImage.setImageURI(uri)
                    binding.playlistImage.visibility = View.VISIBLE
                    newUrl = saveFileToInternalStorage(uri)
                } else {
                    Toast.makeText(requireContext(), "${requireContext().getString(R.string.image_was_not_selected)}", Toast.LENGTH_SHORT).show()
                }
            }

        binding.imagePicker.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.savePlaylistBtn.setOnClickListener {
            val updatedPlaylist = playlist.copy(
                title = binding.playlistTitle.text.toString(),
                description = binding.playlistDescription.text.toString(),
                imageUrl = newUrl
            )
            viewModel.updatePlaylist(updatedPlaylist)
            findNavController().navigateUp()
        }

    }

    private fun saveFileToInternalStorage(uri: Uri) : String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = getFileNameFromUri(uri)
        val fileOutputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        inputStream?.close()
        fileOutputStream?.close()
        val savedFile = requireContext().getFileStreamPath(fileName)
        if (savedFile != null) {
            return savedFile.absolutePath
        }
        return "unknown"
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val contentResolver = requireContext().contentResolver

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        try {
            cursor?.let {
                if (it.moveToFirst()) {
                    val fileNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    fileName = it.getString(fileNameIndex)
                }
            }
        } finally {
            cursor?.close()
        }

        return fileName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setDetailsInfo(){
        binding.apply {
            Glide.with(this@EditPlaylistFragment)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.track_image_placeholder)
                .override(312, 312)
                .centerCrop()
                .into(playlistImage)
            playlistTitle.setText(playlist.title)
            playlistDescription.setText(playlist.description)
        }
    }

    companion object {
        private const val PLAYLIST_DATA = "playlist_data"
    }

    private fun setEnableBtn(isEnable : Boolean){
        binding.savePlaylistBtn.isEnabled = isEnable
        binding.savePlaylistBtn.isClickable = isEnable
        if(isEnable){
            binding.savePlaylistBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.active_btn_color))
        }
        else{
            binding.savePlaylistBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disable_btn_color))
        }
    }

    private fun getPlaylistFromBundle() : Playlist {
        val bundle = arguments
        val playlist_data = bundle?.getString(PLAYLIST_DATA)
        return Playlist.createPlaylistFromJson(playlist_data!!)
    }

}