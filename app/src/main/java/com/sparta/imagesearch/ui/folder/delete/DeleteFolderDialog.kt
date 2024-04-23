package com.sparta.imagesearch.ui.folder.delete

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sparta.imagesearch.databinding.DialogDeleteFolderBinding
import com.sparta.imagesearch.ui.folder.FolderDialogModel
import com.sparta.imagesearch.ui.folder.FolderModel
import com.sparta.imagesearch.ui.folder.OnFolderDialogModelClickListener

interface OnDeleteConfirmListener {
    fun onDeleteConfirm(folderIdList: List<String>)
}

class DeleteFolderDialog(
    private val context: AppCompatActivity,
    private val folderModels: List<FolderModel>
) :
    OnFolderDialogModelClickListener {
    private val TAG = "DeleteFolderDialog"

    private var _binding: DialogDeleteFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private var folderDialogModels = folderModels.map { it.convert() }
    private val folderDialogDeleteAdapter = FolderDialogDeleteAdapter()

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
        folderDialogDeleteAdapter.run {
            onFolderDialogModelClickListener = this@DeleteFolderDialog
            binding.recyclerviewDelete.adapter = this
            submitList(folderDialogModels)
        }
    }


    override fun onFolderDialogModelClick(folderDialogModel: FolderDialogModel) {
        Log.d(TAG, "onFolderDialogModelClick) folderDialogModel.id: ${folderDialogModel.id}")
        folderDialogModels = folderDialogModels.map {
            if (it.id == folderDialogModel.id) it.copy(isChecked = !it.isChecked) else it
        }
        folderDialogDeleteAdapter.submitList(folderDialogModels)
    }

    private fun initCloseButton() {
        binding.bntNegative.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initConfirmButton() {
        binding.bntPositive.setOnClickListener {
            onDeleteConfirmListener?.onDeleteConfirm(
                folderDialogModels.filter { it.isChecked }.map { it.id }
            )
            dialog.dismiss()
        }
    }

    private fun FolderModel.convert() = FolderDialogModel(id, name, colorHex)
}