package com.sparta.imagesearch.ui.folder

import androidx.recyclerview.widget.DiffUtil

sealed interface FolderModelChangePayload {
    data class IsSelected(val isSelected: Boolean) : FolderModelChangePayload
}

object FolderModelDiffCallback : DiffUtil.ItemCallback<FolderModel>() {
    override fun areItemsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: FolderModel, newItem: FolderModel): Any? {
        return when {
            oldItem.isSelected != newItem.isSelected ->
                FolderModelChangePayload.IsSelected(newItem.isSelected)

            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}