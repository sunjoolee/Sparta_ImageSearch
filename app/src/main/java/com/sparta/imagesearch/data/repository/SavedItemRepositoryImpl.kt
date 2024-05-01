package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import com.sparta.imagesearch.di.ApplicationScope
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.entity.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedItemRepositoryImpl @Inject constructor(
    private val savedItemSource: SavedItemDatabase,
    @ApplicationScope private val scope: CoroutineScope,
) : SavedItemRepository {
    private val TAG = "SavedItemRepositoryImpl"

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

    override suspend fun loadSavedItems(): Flow<List<Item>> = savedItemSourceDAO.getAllItems()


    override suspend fun loadFolderSavedItems(folderId: String): Flow<List<Item>> =
        savedItemSourceDAO.getFolderItems(folderId)

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