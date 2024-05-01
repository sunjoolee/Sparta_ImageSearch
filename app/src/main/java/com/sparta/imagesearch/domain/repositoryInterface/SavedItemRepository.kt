package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.entity.Item
import kotlinx.coroutines.flow.Flow

interface SavedItemRepository {
    fun saveSavedItems(savedItems: List<Item>)
    fun deleteSavedItem(item: Item)
    suspend fun loadSavedItems(): Flow<List<Item>>
    suspend fun loadFolderSavedItems(folderId:String): Flow<List<Item>>
    fun deleteFolderSavedItems(folderIds: List<String>)
    fun moveSavedItem(itemId: String, destFolderId: String)
}