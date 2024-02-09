package com.example.myyandexproject.ui.media.fragments

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentCreatePlaylistBinding
import com.example.myyandexproject.ui.media.viewModels.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeName
import java.io.File
import java.io.FileOutputStream

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

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.createPlaylistBtn.setOnClickListener {
            viewModel.createPlaylist()
            findNavController().navigate(R.id.playlistFragment, PlaylistFragment.createArgs(true))
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imagePicker.visibility = View.GONE
                    binding.userImage.setImageURI(uri)
                    binding.userImage.visibility = View.VISIBLE
                    val imagePath = saveFileToInternalStorage(uri)
                    Log.i("myImagePath", imagePath)
                    viewModel.setImageUrl(imagePath)
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

    companion object {
        fun getInstance() = CreatePlaylistFragment()
    }


}