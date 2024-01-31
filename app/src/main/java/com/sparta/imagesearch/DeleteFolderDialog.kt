package com.sparta.imagesearch

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.sparta.imagesearch.data.FolderManager
import com.sparta.imagesearch.databinding.DialogDeleteFolderBinding
import com.sparta.imagesearch.recyclerView.FolderDeleteAdapter
import com.sparta.imagesearch.recyclerView.OnFolderClickListener

class DeleteFolderDialog(private val context: AppCompatActivity) : OnFolderClickListener {

    interface OnDeleteConfirmListener {
        fun onDeleteConfirm(folderIdList: List<String>)
    }

    private var _binding: DialogDeleteFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private val folderIdList = mutableListOf<String>()

    var onDeleteConfirmListener: OnDeleteConfirmListener? = null

    fun show() {
        _binding = DialogDeleteFolderBinding.inflate(context.layoutInflater)

        dialog.run {
            setContentView(binding.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        initRecyclerView()
        initCloseButton()
        initConfirmButton()

        dialog.show()
    }

    private fun initRecyclerView() {
        FolderDeleteAdapter(FolderManager.getFolders()).also {
            it.onFolderClickListener = this@DeleteFolderDialog
            binding.recyclerviewDelete.adapter = it
        }
    }

    override fun onFolderClick(folderId: String) {
        folderIdList.run {
            if (contains(folderId)) remove(folderId)
            else add(folderId)
        }
    }

    private fun initCloseButton() {
        binding.bntNegative.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initConfirmButton() {
        binding.bntPositive.setOnClickListener {
            onDeleteConfirmListener?.onDeleteConfirm(folderIdList)
            dialog.dismiss()
        }
    }
}