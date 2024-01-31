package com.sparta.imagesearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.databinding.FragmentFolderBinding
import com.sparta.imagesearch.recyclerView.GridSpacingItemDecoration
import com.sparta.imagesearch.recyclerView.ItemAdapter
import com.sparta.imagesearch.recyclerView.OnImageClickListener
import com.sparta.imagesearch.util.fromDpToPx


class FolderFragment : Fragment(), OnImageClickListener {
    private val TAG = "FolderFragment"

    private var _binding:FragmentFolderBinding? = null
    private val binding get() = _binding!!

    //private lateinit var folderAdapter: FolderAdapter
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageRecyclerView()
    }

    private fun initImageRecyclerView(){
        itemAdapter = ItemAdapter(MainActivity.savedItems)

        itemAdapter.onImageClickListener = this@FolderFragment
        binding.recyclerviewImage.run {
            adapter = itemAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onImageClick(item: Item) {
        TODO("Not yet implemented")
    }

    override fun onHeartClick(position: Int, item: Item) {
        TODO("Not yet implemented")
    }

    override fun onHeartLongClick(item: Item) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        itemAdapter.notifyDataSetChanged()
    }

}