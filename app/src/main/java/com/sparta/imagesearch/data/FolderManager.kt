package com.sparta.imagesearch.data

import com.sparta.imagesearch.MainActivity
import com.sparta.imagesearch.R

object FolderManager {
    const val DEFAULT_FOLDER_ID = "0"
    val defaultFolder =
        Folder(id = DEFAULT_FOLDER_ID, name = "기본 폴더", colorId = R.color.folder_color1)

    private val folders = mutableListOf<Folder>(
        defaultFolder,
        Folder(name = "폴더 1", colorId = R.color.folder_color2),
        Folder(name = "폴더 2", colorId = R.color.folder_color3),
        Folder(name = "폴더 3", colorId = R.color.folder_color4)
    )
    private var selectedFolderId = DEFAULT_FOLDER_ID
    fun getFolders(): MutableList<Folder> = folders
    fun getSelectedFolderId(): String = selectedFolderId
    fun setSelectedFolderId(folderId: String) {
        selectedFolderId = folderId
    }

    fun addFolder(name: String, colorId: Int) {
        folders.add(Folder(name = name, colorId = colorId))
    }

    fun deleteFolder(folder: Folder) {
        if (folder == defaultFolder) return

        val deleteItems = mutableListOf<Item>()
        MainActivity.savedItems.forEach {
            if((it.folder != null) && (it.folder == folder))
                deleteItems.add(it)
        }
        deleteItems.forEach { it.unsaveItem() }

        folders.remove(folder)
    }
}