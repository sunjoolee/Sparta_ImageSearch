package com.sparta.imagesearch.presentation.folder

import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.Item

interface FolderScreenInputs {
    fun selectFolder(folder: Folder)
    fun unSaveItem(item: Item)
    fun toggleShowAddDialog()
    fun addFolder(name: String, colorHex: String)
    fun toggleDeleteDialog()
    fun deleteFolders(deleteFolderIdList: List<Int>)
    fun toggleMoveDialog(targetItem:Item? = null)
    fun moveFolder(destFolderId: Int)
}