package com.sparta.imagesearch.data

import android.graphics.Color
import com.sparta.imagesearch.MainActivity

object FolderManager {
    const val DEFAULT_FOLDER_ID = "0"
    val defaultFolder = Folder(
        id = DEFAULT_FOLDER_ID,
        name = "기본 폴더",
        color = Color.parseColor(FolderColor.color1.colorHex)
    )

    private val folders = mutableListOf<Folder>(
        defaultFolder,
        Folder(name = "폴더 1", color = Color.parseColor(FolderColor.color2.colorHex)),
        Folder(name = "폴더 2", color = Color.parseColor(FolderColor.color3.colorHex)),
        Folder(name = "폴더 3", color = Color.parseColor(FolderColor.color4.colorHex))
    )
    private var selectedFolderId = DEFAULT_FOLDER_ID
    fun getFolders(): MutableList<Folder> = folders
    fun getSelectedFolderId(): String = selectedFolderId
    fun setSelectedFolderId(folderId: String) {
        selectedFolderId = folderId
    }

    fun addFolder(name: String, color: Int) {
        folders.add(Folder(name = name, color = color))
    }

    fun deleteFolder(folder: Folder) {
        if (folder == defaultFolder) return

        val deleteItems = mutableListOf<Item>()
        MainActivity.savedItems.forEach {
            if ((it.folder != null) && (it.folder == folder))
                deleteItems.add(it)
        }
        deleteItems.forEach { it.unSaveItem() }

        folders.remove(folder)
    }
}