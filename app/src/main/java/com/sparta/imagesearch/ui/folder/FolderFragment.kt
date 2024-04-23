package com.sparta.imagesearch.ui.folder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.databinding.FragmentFolderBinding
import com.sparta.imagesearch.util.GridSpacingItemDecoration
import com.sparta.imagesearch.util.fromDpToPx
import com.sparta.imagesearch.ui.ItemAdapter
import com.sparta.imagesearch.ui.OnHeartClickListener
import com.sparta.imagesearch.ui.OnHeartLongClickListener
import com.sparta.imagesearch.ui.folder.add.AddFolderDialog
import com.sparta.imagesearch.ui.folder.add.OnAddConfirmListener
import com.sparta.imagesearch.ui.folder.delete.DeleteFolderDialog
import com.sparta.imagesearch.ui.folder.delete.OnDeleteConfirmListener
import com.sparta.imagesearch.ui.folder.move.MoveFolderDialog
import com.sparta.imagesearch.ui.folder.move.OnMoveConfirmListener

class FolderFragment : Fragment(),
    OnHeartClickListener,
    OnHeartLongClickListener,
    OnFolderModelClickListener,
    OnDeleteConfirmListener,
    OnMoveConfirmListener,
    OnAddConfirmListener {
    private val TAG = "FolderFragment"

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private val model by viewModels<FolderViewModel>()

    private lateinit var folderAdapter: FolderModelAdapter
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

        initModelObserver()

        initFolderRecyclerView()
        initMoreButton()
        initMoreLayout()

        initImageRecyclerView()
    }

    private fun initModelObserver() {
        with(model) {
            resultFolderModels.observe(viewLifecycleOwner) { folderModels ->
                folderAdapter.submitList(folderModels)
            }
            itemsInFolder.observe(viewLifecycleOwner) { folderItems ->
                itemAdapter.submitList(folderItems)
            }
        }
    }

    private fun initFolderRecyclerView() {
        folderAdapter = FolderModelAdapter()
        folderAdapter.onFolderModelClickListener = this@FolderFragment
        binding.recyclerViewFolder.adapter = folderAdapter
    }

    override fun onFolderModelClick(folderModel: FolderModel) {
        model.selectFolder(folderModel)
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

    private fun showAddFolderDialog() {
        val addDialog = AddFolderDialog(binding.root.context as AppCompatActivity)
        addDialog.onAddConfirmListener = this@FolderFragment
        addDialog.show()
    }

    override fun onAddConfirm(name: String, colorHex: String) {
        model.addFolder(name, colorHex)
    }

    private fun showDeleteFolderDialog() {
        val deleteDialog = DeleteFolderDialog(
            binding.root.context as AppCompatActivity,
            model.folderModels.value!!
        )
        deleteDialog.onDeleteConfirmListener = this@FolderFragment
        deleteDialog.show()
    }

    override fun onDeleteConfirm(folderIdList: List<String>) {
        model.deleteFolders(folderIdList)
    }

    private fun initImageRecyclerView() {
        itemAdapter = ItemAdapter()
        itemAdapter.onHeartClickListener = this@FolderFragment
        itemAdapter.onHeartLongClickListener = this@FolderFragment
        binding.recyclerviewImage.run {
            adapter = itemAdapter
            addItemDecoration(GridSpacingItemDecoration(2, 16f.fromDpToPx()))
        }
    }

    override fun onHeartClick(item: Item) {
        model.unSaveItem(item)
    }

    override fun onHeartLongClick(item: Item) {
        showMoveFolderDialog(item)
    }

    private fun showMoveFolderDialog(item: Item) {
        val moveDialog = MoveFolderDialog(
            binding.root.context as AppCompatActivity,
            item,
            model.folderModels.value!!
        )
        moveDialog.onMoveConfirmListener = this@FolderFragment
        moveDialog.show()
    }

    override fun onMoveConfirm(item: Item, destFolderId: String) {
        model.moveFolder(item, destFolderId)
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