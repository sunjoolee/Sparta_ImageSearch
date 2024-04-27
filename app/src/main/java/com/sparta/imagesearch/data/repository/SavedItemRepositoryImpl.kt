package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import com.sparta.imagesearch.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedItemRepositoryImpl @Inject constructor(
    private val savedItemSource: SavedItemDatabase,
    @ApplicationScope private val scope: CoroutineScope,
) : SavedItemRepository {
    private val savedItemSourceDAO = savedItemSource.getSavedItemDAO()
    override fun saveSavedItems(savedItems: List<Item>) {
        scope.launch {
            savedItems.forEach { savedItemSourceDAO.insertItem(it) }
        }
    }

    override fun deleteSavedItem(item: Item) {
        scope.launch {
            savedItemSourceDAO.deleteItem(item)
        }
    }

    override fun loadSavedItems(): List<Item> {
        val savedItems = mutableListOf<Item>()
        scope.launch {
            savedItems.addAll(savedItemSourceDAO.getAllItems())
        }
        return savedItems
    }

    override fun loadFolderSavedItems(folderId: String): List<Item> {
        val folderItems = mutableListOf<Item>()
        scope.launch {
            folderItems.addAll(savedItemSourceDAO.getFolderItems(folderId))
        }
        return folderItems
    }

    override fun deleteFolderSavedItems(folderIds: List<String>) {
        scope.launch {
            folderIds.forEach { savedItemSourceDAO.deleteFolderItems(it) }
        }
    }

    override fun moveSavedItem(itemId: String, destFolderId: String) {
        scope.launch {
            savedItemSourceDAO.moveItemFolder(itemId, destFolderId)
        }
    }
}