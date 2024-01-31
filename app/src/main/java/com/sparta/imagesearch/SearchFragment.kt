package com.sparta.imagesearch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sparta.imagesearch.data.Image
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.Video
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.recyclerView.GridSpacingItemDecoration
import com.sparta.imagesearch.recyclerView.ItemAdapter
import com.sparta.imagesearch.recyclerView.OnItemClickListener
import com.sparta.imagesearch.retrofit.ImageResponse
import com.sparta.imagesearch.retrofit.SearchClient
import com.sparta.imagesearch.retrofit.VideoResponse
import com.sparta.imagesearch.util.fromDpToPx
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), OnItemClickListener {
    private val TAG = "SearchFragment"

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUIState()

        initImageRecyclerView()
        setSearchButtonOnClickListener()
    }

    private fun loadUIState(){
        val keyword = loadKeyword()
        binding.etSearch.setText(keyword)
        lifecycleScope.launch {
            communicateImageSearchNetwork(keyword)
        }
    }
    private fun loadKeyword(): String {
        val pref = requireActivity().getSharedPreferences("pref", 0)
        return pref.getString("keyword", "") ?: ""
    }

    private fun saveKeyword(keyword: String) {
        val pref = requireActivity().getSharedPreferences("pref", 0)
        pref.edit()
            .putString("keyword", keyword)
            .apply()
    }

    private fun initImageRecyclerView() {
        itemAdapter = ItemAdapter(mutableListOf<Item>())

        itemAdapter.onItemClickListener = this@SearchFragment
        binding.recyclerviewImage.run {
            adapter = itemAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onItemImageClick(item: Item) {
        //TODO Not yet implemented
    }

    override fun onItemHeartClick(position: Int, item: Item) {
        item.run {
            if (isSaved()) unsaveItem() else saveItem()
        }
        itemAdapter.notifyItemChanged(position)
    }

    override fun onItemHeartLongClick(item: Item) {
        //TODO Not yet implemented
    }

    private fun setSearchButtonOnClickListener() {
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)
            smoothScrollToTop()

            val keyword = binding.etSearch.text.toString()
            saveKeyword(keyword)
            lifecycleScope.launch {
                communicateImageSearchNetwork(keyword)
            }
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

    private suspend fun communicateImageSearchNetwork(query: String) {
        var imageResponse: ImageResponse? = null
        var videoResponse: VideoResponse? = null

        val job = lifecycleScope.launch {
            try {
                Log.d(TAG, "getting image response...")
                imageResponse = SearchClient.searchNetWork.getImageResponse(query)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
            try {
                Log.d(TAG, "getting video response...")
                videoResponse = SearchClient.searchNetWork.getVideoResponse(query)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
        }
        job.join()

        val newDataset = getNewDataset(imageResponse, videoResponse)
        updateImageRecyclerView(newDataset)
    }

    private fun updateImageRecyclerView(newDataset: MutableList<Item>) {
        itemAdapter.changeDataset(newDataset)
    }

    private fun getNewDataset(
        imageResponse: ImageResponse?,
        videoResponse: VideoResponse?
    ): MutableList<Item> {
        val newDataset = mutableListOf<Item>()
        newDataset += imageResponseToDataset(imageResponse)
        newDataset += videoResponseToDataset(videoResponse)

        newDataset.run {
            sortBy { it.time }
            reverse()
        }
        return newDataset.toMutableList()
    }

    private fun imageResponseToDataset(imageResponse: ImageResponse?): MutableList<Item> {
        Log.d(TAG, "image response to dataset")
        val newDataset = mutableListOf<Item>()
        imageResponse?.documents?.forEach {
            newDataset.add(Image.createFromImageDocument(it))
        }
        return newDataset
    }

    private fun videoResponseToDataset(videoResponse: VideoResponse?): MutableList<Item> {
        Log.d(TAG, "video response to dataset")
        val newDataset = mutableListOf<Item>()
        videoResponse?.documents?.forEach {
            newDataset.add(Video.createFromVideoDocument(it))
        }
        return newDataset
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        itemAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}