package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.flow.Flow

interface SavedItemRepository {
    fun saveSavedItems(items: List<Item>)
    fun deleteSavedItem(item: Item)
    suspend fun loadSavedItems(): Flow<List<Item>>
    suspend fun loadFolderSavedItems(folderId: Int): Flow<List<Item>>
    fun deleteFolderSavedItems(folderIds: List<Int>)
    fun moveSavedItem(imageUrl: String, destFolderId: Int)
}