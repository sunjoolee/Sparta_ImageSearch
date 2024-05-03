package com.sparta.imagesearch.presentation.folder


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sparta.imagesearch.databinding.FragmentFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderFragment : Fragment() {
    private val TAG = "FolderFragment"

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private val model by viewModels<FolderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        binding.composeViewFolderScreen.setContent {
            FolderScreen(viewModel = model)
        }
        return binding.root
    }

    override fun onResume() {
        Log.d(TAG, "onResume) called")
        model.loadState()
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause) called")
        model.saveState()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}