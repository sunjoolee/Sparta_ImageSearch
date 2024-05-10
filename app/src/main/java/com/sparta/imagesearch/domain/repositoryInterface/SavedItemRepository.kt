package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.flow.Flow

interface SavedItemRepository {
    suspend fun saveSavedItem(item: Item)
    suspend fun deleteSavedItem(item: Item)
    suspend fun getAllSavedItems(): Flow<List<Item>>
    suspend fun deleteSavedItemsByFolderId(folderIds: List<Int>)
    suspend fun moveSavedItem(imageUrl: String, targetFolderId: Int)
}