package com.sparta.imagesearch

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.sparta.imagesearch.data.FolderManager
import com.sparta.imagesearch.databinding.DialogDeleteFolderBinding
import com.sparta.imagesearch.recyclerView.FolderDialogAdapter
import com.sparta.imagesearch.recyclerView.OnFolderDialogClickListener

class DeleteFolderDialog(private val context: AppCompatActivity) : OnFolderDialogClickListener {

    interface OnConfirmListener {
        fun onConfirm(folderIdList: List<String>)
    }

    private var _binding: DialogDeleteFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private val folderIdList = mutableListOf<String>()

    var onConfirmListener: OnConfirmListener? = null

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
        FolderDialogAdapter(FolderManager.getFolders()).also {
            it.onFolderDialogClickListener = this@DeleteFolderDialog
            binding.recyclerviewDelete.adapter = it
        }
    }

    override fun onClick(folderId: String) {
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
            onConfirmListener?.onConfirm(folderIdList)
            dialog.dismiss()
        }
    }
}