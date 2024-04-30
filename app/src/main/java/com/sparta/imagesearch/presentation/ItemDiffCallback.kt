package com.sparta.imagesearch.presentation

import androidx.recyclerview.widget.DiffUtil
import com.sparta.imagesearch.entity.Item

sealed interface ItemChangePayload {
    data class FolderIdChange(val folderId: String) : ItemChangePayload
}

object ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
        return when {
            oldItem.folderId != newItem.folderId ->
                ItemChangePayload.FolderIdChange(newItem.folderId)

            else -> super.getChangePayload(oldItem, newItem)
        }
    }
}