package com.sparta.imagesearch.data

import com.sparta.imagesearch.MainActivity

enum class ItemType {
    Image,
    Video,
    ProgressBar
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

    fun unSaveItem() {
        MainActivity.savedItems.remove(this)
        this.folder = null
    }

    companion object{
       fun newProgressBar():Item = Item(type=ItemType.ProgressBar, time ="")
    }
}