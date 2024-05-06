package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedItem(savedItem: SavedItemEntity)

    @Delete
    fun deleteSavedItem(savedItem: SavedItemEntity)

    @Query("SELECT * FROM saved_item_table")
    fun getAllSavedItems(): Flow<List<SavedItemEntity>>

    @Query("SELECT * FROM saved_item_table WHERE folderId = :folderId")
    fun getSavedItemsInFolder(folderId:String): Flow<List<SavedItemEntity>>

    @Query("DELETE FROM saved_item_table WHERE folderId = :folderId")
    fun deleteSavedItemsInFolder(folderId:String)

    @Query("UPDATE saved_item_table SET folderId = :destFolderId WHERE imageUrl = :imageUrl")
    fun moveSavedItemFolder(imageUrl:String, destFolderId:String)
}