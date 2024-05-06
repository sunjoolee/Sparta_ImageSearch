package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.flow.Flow

interface SavedItemRepository {
    fun saveSavedItems(items: List<Item>)
    fun deleteSavedItem(item: Item)
    suspend fun loadSavedItems(): Flow<List<Item>>
    suspend fun loadFolderSavedItems(folderId:String): Flow<List<Item>>
    fun deleteFolderSavedItems(folderIds: List<String>)
    fun moveSavedItem(imageUrl: String, destFolderId: String)
}