package com.sparta.imagesearch.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
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
    private var searchPage: Int = 1

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
        initUpButton()
        initSearchButton()
    }

    private fun loadUIState() {
        val keyword = loadKeyword()
        binding.etSearch.setText(keyword)
        lifecycleScope.launch { communicateImageSearchNetwork() }
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
            itemAnimator = null
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
            initOnScrollListener()
        }
    }

    private fun RecyclerView.initOnScrollListener() {
        setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                setUpButtonVisibility(!(newState == RecyclerView.SCROLL_STATE_IDLE))

                if (!binding.recyclerviewImage.canScrollVertically(1)) {
                    infiniteScroll()
                }
            }
        })
    }

    private fun infiniteScroll(){
        Log.d(TAG, "end of item RecyclerView")
        lifecycleScope.launch { communicateImageSearchNetwork() }
    }

    private fun initUpButton() {
        binding.fabUp.setOnClickListener {
            smoothScrollToTop()
        }
    }

    private fun setUpButtonVisibility(flag: Boolean) {
        if (flag) {
            binding.fabUp.isVisible = true
        } else {
            Handler().postDelayed(Runnable {
                binding.fabUp.isVisible = false
            }, 2500)
        }
    }

    override fun onItemImageClick(item: Item) {
        //TODO Not yet implemented
    }

    override fun onItemHeartClick(position: Int, item: Item) {
        item.run {
            if (isSaved()) unSaveItem() else saveItem()
        }
        itemAdapter.notifyItemChanged(position)
    }

    override fun onItemHeartLongClick(item: Item) {
        //TODO Not yet implemented
    }

    private fun initSearchButton() {
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)
            smoothScrollToTop()

            val keyword = binding.etSearch.text.toString()
            saveKeyword(keyword)
            lifecycleScope.launch { communicateImageSearchNetwork()}
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

    private suspend fun communicateImageSearchNetwork() {
        val keyword = loadKeyword()

        var imageResponse: ImageResponse? = null
        var videoResponse: VideoResponse? = null

        val job = lifecycleScope.launch {
            try {
                imageResponse =
                    SearchClient.searchNetWork.getImageResponse(keyword, page = searchPage)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
            try {
                videoResponse =
                    SearchClient.searchNetWork.getVideoResponse(keyword, page = searchPage)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
            searchPage++
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
        val newDataset = mutableListOf<Item>().apply {
            addAll(imageResponseToDataset(imageResponse))
            addAll(videoResponseToDataset(videoResponse))
            sortBy { it.time }
            reverse()
        }

        return (itemAdapter.dataset + newDataset).toMutableList()
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