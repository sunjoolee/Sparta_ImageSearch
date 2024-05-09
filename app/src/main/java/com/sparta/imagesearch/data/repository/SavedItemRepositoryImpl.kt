package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.data.mappers.toSavedItem
import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedItemRepositoryImpl @Inject constructor(
    private val savedItemSource: SavedItemDatabase
) : SavedItemRepository {
    private val TAG = "SavedItemRepositoryImpl"

    private val savedItemSourceDAO = savedItemSource.getSavedItemDAO()
    override suspend fun saveSavedItem(item: Item) = withContext(Dispatchers.IO) {
        savedItemSourceDAO.upsertItem(item.toSavedItem())
    }

    override suspend fun deleteSavedItem(item: Item) = withContext(Dispatchers.IO) {
        savedItemSourceDAO.deleteItem(item.toSavedItem())
    }

    override suspend fun getAllSavedItems(): Flow<List<Item>> = withContext(Dispatchers.IO) {
        savedItemSourceDAO.getAllItems().transform{
            emit(it.map { savedItem -> savedItem.toItem() })
        }
    }

    override suspend fun getSavedItemsByFolderId(folderId: Int): Flow<List<Item>> =
        withContext(Dispatchers.IO) {
            savedItemSourceDAO.getSavedItemsByFolderId(folderId)
                .transform {
                    emit(it.map { savedItem -> savedItem.toItem() })
                }
        }

    override suspend fun deleteSavedItemsByFolderId(folderIds: List<Int>) =
        withContext(Dispatchers.IO) {
            folderIds.forEach { savedItemSourceDAO.deleteSavedItemsByFolderId(it) }
        }

    override suspend fun moveSavedItem(imageUrl: String, destFolderId: Int) =
        withContext(Dispatchers.IO) {
            savedItemSourceDAO.moveItemFolder(imageUrl, destFolderId)
        }
}