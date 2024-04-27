package com.sparta.imagesearch.ui.folder.dialog.move

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sparta.imagesearch.R
import com.sparta.imagesearch.databinding.RecyclerViewItemFolderDialogBinding
import com.sparta.imagesearch.ui.folder.dialog.FolderDialogModel
import com.sparta.imagesearch.ui.folder.dialog.FolderDialogModelChangePayload
import com.sparta.imagesearch.ui.folder.dialog.FolderDialogModelDiffCallback
import com.sparta.imagesearch.ui.folder.dialog.OnFolderDialogModelClickListener

class FolderDialogMoveAdapter() :
    ListAdapter<FolderDialogModel, FolderDialogMoveAdapter.Holder>(FolderDialogModelDiffCallback) {
    private val TAG = "FolderDialogMoveAdapter"

    var onFolderDialogModelClickListener: OnFolderDialogModelClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerViewItemFolderDialogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        when (val lastPayload = payloads.lastOrNull()) {
            is FolderDialogModelChangePayload.IsChecked ->
                holder.bindIsChecked(lastPayload.isChecked)

            else -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class Holder(private val binding: RecyclerViewItemFolderDialogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val checkImageView = binding.ivCheck
        private val folderImageView = binding.ivFolder
        private val nameTextView = binding.tvFolderName

        fun bind(folderDialogModel: FolderDialogModel) {
            folderImageView.imageTintList = ColorStateList.valueOf(
                Color.parseColor(folderDialogModel.colorHex)
            )
            nameTextView.text = folderDialogModel.name
            bindIsChecked(folderDialogModel.isChecked)

            setListener(folderDialogModel)
        }

        fun bindIsChecked(isChecked: Boolean) {
            checkImageView.setImageResource(
                if (isChecked) R.drawable.icon_select_full
                else R.drawable.icon_select_empty
            )
        }

        private fun setListener(folderDialogModel: FolderDialogModel) {
            itemView.setOnClickListener {
                onFolderDialogModelClickListener?.onFolderDialogModelClick(folderDialogModel)
            }
        }
    }
}