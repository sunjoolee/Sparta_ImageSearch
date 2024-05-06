package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.data.mappers.toSavedItem
import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import com.sparta.imagesearch.di.ApplicationScope
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedItemRepositoryImpl @Inject constructor(
    private val savedItemSource: SavedItemDatabase,
    @ApplicationScope private val scope: CoroutineScope,
) : SavedItemRepository {
    private val TAG = "SavedItemRepositoryImpl"

    private val savedItemSourceDAO = savedItemSource.getSavedItemDAO()
    override fun saveSavedItems(items: List<Item>) {
        scope.launch {
            items.map{it.toSavedItem()}.forEach { savedItemSourceDAO.insertItem(it) }
        }
    }

    override fun deleteSavedItem(item: Item) {
        scope.launch {
            savedItemSourceDAO.deleteItem(item.toSavedItem())
        }
    }

    override suspend fun loadSavedItems(): Flow<List<Item>> =
        savedItemSourceDAO.getAllItems().transform { it.map { it.toItem() } }


    override suspend fun loadFolderSavedItems(folderId: String): Flow<List<Item>> =
        savedItemSourceDAO.getFolderItems(folderId).transform { it.map { it.toItem() } }

    override fun deleteFolderSavedItems(folderIds: List<String>) {
        scope.launch {
            folderIds.forEach { savedItemSourceDAO.deleteFolderItems(it) }
        }
    }

    override fun moveSavedItem(imageUrl: String, destFolderId: String) {
        scope.launch {
            savedItemSourceDAO.moveItemFolder(imageUrl, destFolderId)
        }
    }
}