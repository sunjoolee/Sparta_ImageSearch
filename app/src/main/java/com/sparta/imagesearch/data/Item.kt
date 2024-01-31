package com.sparta.imagesearch.data

import com.sparta.imagesearch.MainActivity

enum class ItemType {
    Image,
    Video
}

open class Item(
    val type: ItemType,
    open var folder: ItemFolder? = null,
    open val time: String
) {
    fun isSaved(): Boolean = (folder != null)

    fun saveItem(folder: ItemFolder? = null) {
        this.folder = folder ?: ImageFolderManager.defaultFolder
        MainActivity.savedItems.add(this)
    }

    fun unsaveItem() {
        MainActivity.savedItems.remove(this)
        this.folder = null
    }
}