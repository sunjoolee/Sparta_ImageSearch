package com.sparta.imagesearch.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import com.sparta.imagesearch.data.FolderManager
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.databinding.DialogMoveFolderBinding
import com.sparta.imagesearch.recyclerView.FolderMoveAdapter
import com.sparta.imagesearch.recyclerView.OnFolderClickListener

class MoveFolderDialog(private val context: AppCompatActivity, val item: Item) : OnFolderClickListener {

    interface OnMoveConfirmListener {
        fun onMoveConfirm(item:Item, folderId:String)
    }

    private var _binding: DialogMoveFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)
    private var checkedFolderId = item.folder!!.id

    var onMoveConfirmListener: OnMoveConfirmListener? = null

    fun show() {
        _binding = DialogMoveFolderBinding.inflate(context.layoutInflater)

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
        FolderMoveAdapter(FolderManager.getFolders(), checkedFolderId).also {
            it.onFolderClickListener = this@MoveFolderDialog
            binding.recyclerviewMove.adapter = it
        }
    }

    override fun onFolderClick(folderId: String) {
        checkedFolderId = folderId
    }

    private fun initCloseButton() {
        binding.bntNegative.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initConfirmButton() {
        binding.bntPositive.setOnClickListener {
            onMoveConfirmListener?.onMoveConfirm(item, checkedFolderId)
            dialog.dismiss()
        }
    }
}