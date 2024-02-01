package com.sparta.imagesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sparta.imagesearch.data.FolderManager
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.databinding.FragmentFolderBinding
import com.sparta.imagesearch.recyclerView.FolderAdapter
import com.sparta.imagesearch.recyclerView.GridSpacingItemDecoration
import com.sparta.imagesearch.recyclerView.ItemAdapter
import com.sparta.imagesearch.recyclerView.OnFolderClickListener
import com.sparta.imagesearch.recyclerView.OnItemClickListener
import com.sparta.imagesearch.util.fromDpToPx

class FolderFragment : Fragment(), OnItemClickListener, OnFolderClickListener,
    DeleteFolderDialog.OnDeleteConfirmListener, MoveFolderDialog.OnMoveConfirmListener,
    AddFolderDialog.OnAddConfirmListener {
    private val TAG = "FolderFragment"

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private lateinit var folderAdapter: FolderAdapter
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

        initFolderRecyclerView()
        initMoreButton()
        initMoreLayout()

        initImageRecyclerView()
    }

    private fun initFolderRecyclerView() {
        folderAdapter = FolderAdapter(FolderManager.getFolders())
        folderAdapter.onFolderClickListener = this@FolderFragment
        binding.recyclerViewFolder.adapter = folderAdapter
    }

    override fun onFolderClick(folderId: String) {
        FolderManager.setSelectedFolderId(folderId)
        refreshItemRecyclerView()
    }


    private fun initMoreButton() {
        binding.ivMore.setOnClickListener {
            binding.layoutMore.isVisible = !binding.layoutMore.isVisible
        }
    }

    private fun initMoreLayout() {
        binding.tvMoreAdd.setOnClickListener {
            showAddFolderDialog()
        }
        binding.tvMoreDelete.setOnClickListener {
            showDeleteFolderDialog()
        }
    }

    private fun showAddFolderDialog(){
        val addDialog = AddFolderDialog(binding.root.context as AppCompatActivity)
        addDialog.onAddConfirmListener = this@FolderFragment
        addDialog.show()
    }

    override fun onAddConfirm(name: String, color: Int) {
        FolderManager.addFolder(name, color)
        folderAdapter.notifyDataSetChanged()
    }
    private fun showDeleteFolderDialog() {
        val deleteDialog = DeleteFolderDialog(binding.root.context as AppCompatActivity)
        deleteDialog.onDeleteConfirmListener = this@FolderFragment
        deleteDialog.show()
    }

    override fun onDeleteConfirm(folderIdList: List<String>) {
        val deleteFolders = FolderManager.getFolders().filter { folderIdList.contains(it.id) }
        deleteFolders.forEach { FolderManager.deleteFolder(it) }

        if (deleteFolders.find { it.id == FolderManager.getSelectedFolderId() } != null) {
            FolderManager.setSelectedFolderId(FolderManager.DEFAULT_FOLDER_ID)
            refreshItemRecyclerView()
        }
        folderAdapter.notifyDataSetChanged()
    }

    private fun initImageRecyclerView() {
        itemAdapter = ItemAdapter(getFolderDataset(FolderManager.getSelectedFolderId()))
        itemAdapter.onItemClickListener = this@FolderFragment
        binding.recyclerviewImage.run {
            adapter = itemAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onItemImageClick(item: Item) {
        //TODO("Not yet implemented")
    }

    override fun onItemHeartClick(position: Int, item: Item) {
        item.unSaveItem()
        itemAdapter.notifyItemRemoved(position)
    }

    override fun onItemHeartLongClick(item: Item) {
        showMoveFolderDialog(item)
    }

    private fun showMoveFolderDialog(item: Item) {
        val moveDialog = MoveFolderDialog(binding.root.context as AppCompatActivity, item)
        moveDialog.onMoveConfirmListener = this@FolderFragment
        moveDialog.show()
    }

    override fun onMoveConfirm(item: Item, checkedFolderId: String) {
        item.folder = FolderManager.getFolders().find { it.id == checkedFolderId }!!
        refreshItemRecyclerView()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    private fun refreshItemRecyclerView(){
        itemAdapter.changeDataset(
            getFolderDataset(FolderManager.getSelectedFolderId())
        )
    }
    private fun getFolderDataset(folderId: String) = MainActivity.savedItems.filter {
        it.folder?.id == folderId
    }.toMutableList()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}