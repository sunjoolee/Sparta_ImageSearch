package com.sparta.imagesearch.view.folder

import androidx.recyclerview.widget.DiffUtil

sealed interface FolderDialogModelChangePayload {
    data class IsChecked(val isChecked: Boolean) : FolderDialogModelChangePayload
}

object FolderDialogModelDiffCallback : DiffUtil.ItemCallback<FolderDialogModel>() {
    override fun areItemsTheSame(oldItem: FolderDialogModel, newItem: FolderDialogModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FolderDialogModel, newItem: FolderDialogModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: FolderDialogModel, newItem: FolderDialogModel): Any? {
        return when {
            oldItem.isChecked != newItem.isChecked ->
                FolderDialogModelChangePayload.IsChecked(newItem.isChecked)

            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}