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
import com.sparta.imagesearch.data.ImageManager
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.recyclerView.GridSpacingItemDecoration
import com.sparta.imagesearch.recyclerView.ImageAdapter
import com.sparta.imagesearch.recyclerView.OnImageClickListener
import com.sparta.imagesearch.retrofit.ImageResponse
import com.sparta.imagesearch.retrofit.SearchClient
import com.sparta.imagesearch.util.fromDpToPx
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), OnImageClickListener {
    private val TAG = "SearchFragment"

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: ImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageRecyclerView()
        setSearchButtonOnClickListener()
    }

    private fun initImageRecyclerView() {
        imageAdapter = ImageAdapter(mutableListOf<Image>())

        imageAdapter.onImageClickListener = this@SearchFragment
        binding.recyclerviewImage.run {
            adapter = imageAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onImageClick(image: Image) {
        Log.d(TAG, "onItemClick")
        //TODO Not yet implemented
    }
    override fun onHeartClick(image: Image) {
        Log.d(TAG, "onHeartClick")

        if(!ImageManager.isSaved(image)) ImageManager.saveImage(image)
        else ImageManager.unsaveImage(image)

        imageAdapter.notifyDataSetChanged()
    }

    override fun onHeartLongClick(image: Image) {
        Log.d(TAG, "onHeartLongClick")
        //TODO 폴더 이동하기
    }

    private fun setSearchButtonOnClickListener() {
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)

            val keyword = binding.etSearch.text.toString()
            lifecycleScope.launch { communicateImageSearchNetwork(keyword) }
        }
    }
    private fun hideKeyboard(view:View){
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private suspend fun communicateImageSearchNetwork(query: String) {
        var imageResponse: ImageResponse? = null
        val job = lifecycleScope.launch {
            try {
                imageResponse = SearchClient.searchNetWork.getImageResponse(query)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
        }
        job.join()

        val newDataset = imageResponseToDataset(imageResponse)
        updateImageRecyclerView(newDataset)
    }

    private fun updateImageRecyclerView(newDataset: MutableList<Image>) {
        imageAdapter.changeDataset(newDataset)
    }

    private fun imageResponseToDataset(imageResponse: ImageResponse?): MutableList<Image> {
        val newDataset = mutableListOf<Image>()
        imageResponse?.documents?.forEach {
            newDataset.add(Image.createFromImageDocument(it))
        }
        return newDataset
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}