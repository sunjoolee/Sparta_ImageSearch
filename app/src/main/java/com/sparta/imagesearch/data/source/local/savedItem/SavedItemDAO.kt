package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDAO {
    @Upsert
    fun upsertItem(savedItem: SavedItem)

    @Delete
    fun deleteItem(savedI: SavedItem)

    @Query("SELECT * FROM saved_item_table")
    fun getAllItems(): Flow<List<SavedItem>>

    @Query("SELECT * FROM saved_item_table WHERE folderId = :folderId")
    fun getFolderItems(folderId:Int): Flow<List<SavedItem>>

    @Query("DELETE FROM saved_item_table WHERE folderId = :folderId")
    fun deleteFolderItems(folderId:Int)

    @Query("UPDATE saved_item_table SET folderId = :destFolderId WHERE imageUrl = :imageUrl")
    fun moveItemFolder(imageUrl:String, destFolderId:Int)
}