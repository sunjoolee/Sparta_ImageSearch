package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.data.mappers.toSavedItemEntity
import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import com.sparta.imagesearch.di.ApplicationScope
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
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
    override fun saveSavedItems(savedItems: List<Item>) {
        scope.launch {
            savedItems
                .map { it.toSavedItemEntity() }
                .forEach { savedItemSourceDAO.insertSavedItem(it) }
        }
    }

    override fun deleteSavedItem(item: Item) {
        scope.launch {
            savedItemSourceDAO.deleteSavedItem(item.toSavedItemEntity())
        }
    }

    override suspend fun loadSavedItems(): Flow<List<Item>> =
        savedItemSourceDAO.getAllSavedItems()
            .transform { items -> items.map { it.toItem() } }


    override suspend fun loadSavedItemsInFolder(folderId: String): Flow<List<Item>> =
        savedItemSourceDAO.getSavedItemsInFolder(folderId)
            .transform { items -> items.map { it.toItem() } }

    override fun deleteSavedItemsInFolders(folderIds: List<String>) {
        scope.launch {
            folderIds.forEach { savedItemSourceDAO.deleteSavedItemsInFolder(it) }
        }
    }

    override fun moveSavedItem(imageUrl: String, destFolderId: String) {
        scope.launch {
            savedItemSourceDAO.moveSavedItemFolder(imageUrl, destFolderId)
        }
    }
}