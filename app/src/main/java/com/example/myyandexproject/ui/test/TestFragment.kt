package com.example.myyandexproject.ui.test

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myyandexproject.R
import com.example.myyandexproject.ui.PlaylistsState
import java.io.File

class TestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val internalDirectory = context?.filesDir
        Log.i("test_file", "$internalDirectory")
        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}