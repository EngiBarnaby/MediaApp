package com.example.myyandexproject.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.myyandexproject.R
import com.example.myyandexproject.databinding.FragmentMainMediaBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainMediaFragment : Fragment() {
    private var _binding: FragmentMainMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    companion object {
        fun getInstance(isCreated: Boolean? = null): MainMediaFragment {
            val fragment = MainMediaFragment()
            fragment.arguments = createArgs(isCreated)
            return fragment
        }
        private const val ARGS_IS_CREATED = "created"
        fun createArgs(isCreated: Boolean?): Bundle =
            bundleOf(ARGS_IS_CREATED to isCreated)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMainMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaLibraryAdapter(childFragmentManager, lifecycle)

        val isPlaylistCreated = requireArguments()?.getBoolean(ARGS_IS_CREATED, false)

        if(isPlaylistCreated){
            Toast.makeText(requireContext(), "Новый плейлист был создан", Toast.LENGTH_SHORT).show()
        }


        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.table_name_favorite)
                1 -> tab.text = getString(R.string.table_name_playlist)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }
}