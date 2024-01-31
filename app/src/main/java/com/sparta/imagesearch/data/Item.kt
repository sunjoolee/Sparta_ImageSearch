package com.sparta.imagesearch.data

import com.sparta.imagesearch.MainActivity

enum class ItemType {
    Image,
    Video
}

open class Item(
    val type: ItemType,
    open var folder: Folder? = null,
    open val time: String
) {
    fun isSaved(): Boolean = (folder != null)

    fun saveItem(folder: Folder? = null) {
        this.folder = folder ?: FolderManager.defaultFolder
        MainActivity.savedItems.add(this)
    }

    fun unsaveItem() {
        MainActivity.savedItems.remove(this)
        this.folder = null
    }
}