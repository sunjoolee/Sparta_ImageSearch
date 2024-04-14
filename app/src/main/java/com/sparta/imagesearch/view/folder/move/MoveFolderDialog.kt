package com.sparta.imagesearch.view.folder.move

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.databinding.DialogMoveFolderBinding
import com.sparta.imagesearch.view.folder.FolderDialogModel
import com.sparta.imagesearch.view.folder.FolderModel
import com.sparta.imagesearch.view.folder.OnFolderDialogModelClickListener

interface OnMoveConfirmListener {
    fun onMoveConfirm(item: Item, destFolderId: String)
}
class MoveFolderDialog(
    private val context: AppCompatActivity,
    val item: Item,
    private val folderModels: List<FolderModel>
) :
    DialogFragment(),
    OnFolderDialogModelClickListener {

    private var _binding: DialogMoveFolderBinding? = null
    private val binding get() = _binding!!

    private val dialog = Dialog(context)

    private var destFolderId = item.folderId
    private var folderDialogModels = folderModels.map{it.convert()}

    private val folderDialogModelMoveAdapter = FolderDialogMoveAdapter()
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
        folderDialogModelMoveAdapter.run {
            onFolderDialogModelClickListener = this@MoveFolderDialog
            binding.recyclerviewMove.adapter = this
            submitList(folderDialogModels)
        }
    }

    override fun onFolderDialogModelClick(folderDialogModel: FolderDialogModel) {
        destFolderId = folderDialogModel.id
        folderDialogModels = folderDialogModels.map { it.copy(isChecked = (it.id == destFolderId)) }
        folderDialogModelMoveAdapter.submitList(folderDialogModels)
    }

    private fun initCloseButton() {
        binding.bntNegative.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initConfirmButton() {
        binding.bntPositive.setOnClickListener {
            onMoveConfirmListener?.onMoveConfirm(item, destFolderId)
            dialog.dismiss()
        }
    }

    private fun FolderModel.convert() =
        FolderDialogModel(id, name, colorHex, id == destFolderId)
}