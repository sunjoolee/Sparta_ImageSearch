package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.entity.Item

interface SavedItemRepository {
    fun saveSavedItems(savedItems: List<Item>)
    fun deleteSavedItem(item: Item)
    fun loadSavedItems(): List<Item>
    fun loadFolderSavedItems(folderId:String): List<Item>
    fun deleteFolderSavedItems(folderIds: List<String>)
    fun moveSavedItem(itemId: String, destFolderId: String)
}