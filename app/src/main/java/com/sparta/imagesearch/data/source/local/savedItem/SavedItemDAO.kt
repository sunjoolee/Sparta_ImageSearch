package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(savedItem: SavedItem)

    @Delete
    fun deleteItem(savedI: SavedItem)

    @Query("SELECT * FROM saved_item_table")
    fun getAllItems(): Flow<List<SavedItem>>

    @Query("SELECT * FROM saved_item_table WHERE folderId = :folderId")
    fun getFolderItems(folderId:String): Flow<List<SavedItem>>

    @Query("DELETE FROM saved_item_table WHERE folderId = :folderId")
    fun deleteFolderItems(folderId:String)

    @Query("UPDATE saved_item_table SET folderId = :destFolderId WHERE imageUrl = :imageUrl")
    fun moveItemFolder(imageUrl:String, destFolderId:String)
}