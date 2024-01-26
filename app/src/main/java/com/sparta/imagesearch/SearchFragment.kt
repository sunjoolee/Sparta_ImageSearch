package com.sparta.imagesearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sparta.imagesearch.data.RecyclerViewImage
import com.sparta.imagesearch.databinding.FragmentSearchBinding
import com.sparta.imagesearch.recyclerView.RecyclerViewImageAdapter
import com.sparta.imagesearch.retrofit.ImageResponse
import com.sparta.imagesearch.retrofit.SearchClient
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
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

        //Retrofit Test
        lifecycleScope.launch{
            communicateImageSearchNetwork("강아지")
        }
    }

    private fun initImageRecyclerView(){
        binding.recyclerviewImage.run{
            adapter = RecyclerViewImageAdapter(mutableListOf<RecyclerViewImage>())
        }

    }

    private suspend fun communicateImageSearchNetwork(query:String) {
        var imageResponse:ImageResponse? = null

        val job = lifecycleScope.launch{
            try {
                imageResponse = SearchClient.searchNetWork.getImage(query)
            }catch (e:Exception){
                e.printStackTrace()
                cancel()
            }
        }
        job.join()

        Log.d(TAG, "imageSearchResponse) size: ${imageResponse?.documents?.size}")
        imageResponse?.documents?.forEach {
            Log.d(TAG, "imageSearchResponse) url: ${it.image_url}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}