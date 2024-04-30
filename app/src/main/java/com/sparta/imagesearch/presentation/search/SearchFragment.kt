package com.sparta.imagesearch.presentation.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.entity.Item
import com.sparta.imagesearch.presentation.GridSpacingItemDecoration
import com.sparta.imagesearch.presentation.ItemAdapter
import com.sparta.imagesearch.presentation.OnHeartClickListener
import com.sparta.imagesearch.util.fromDpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnHeartClickListener {
    private val TAG = "SearchFragment"

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val model by viewModels<SearchViewModel>()

    private var itemAdapter = ItemAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageRecyclerView()
        setSearchButtonOnClickListener()

        collectStateFlow()
    }

    private fun collectStateFlow() {
        lifecycleScope.launch {
            model.keyword.collect { keyword ->
                binding.etSearch.setText(keyword)
            }
        }
        lifecycleScope.launch {
            model.resultItems.stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            ).collect { resultItems ->
                itemAdapter.submitList(resultItems)
            }
        }
    }

    private fun initImageRecyclerView() {
        itemAdapter.onHeartClickListener = this@SearchFragment
        binding.recyclerviewImage.run {
            adapter = itemAdapter
            itemAnimator = null
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onHeartClick(item: Item) {
        model.saveItem(item)
    }

    private fun setSearchButtonOnClickListener() {
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)
            smoothScrollToTop()

            model.search(binding.etSearch.text.toString())
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun smoothScrollToTop() {
        binding.recyclerviewImage.smoothScrollToPosition(0)
    }

    override fun onPause() {
        Log.d(TAG, "onPause) called")
        model.saveState()
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG, "onResume) called")
        model.loadState()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}