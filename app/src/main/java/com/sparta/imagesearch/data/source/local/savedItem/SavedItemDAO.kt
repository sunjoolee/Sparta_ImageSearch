package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparta.imagesearch.entity.Item

@Dao
interface SavedItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM saved_item_table")
    suspend fun getAllItems(): List<Item>

    @Query("SELECT * FROM saved_item_table WHERE folderId = :folderId")
    suspend fun getFolderItems(folderId:String): List<Item>

    @Query("DELETE FROM saved_item_table WHERE folderId = :folderId")
    suspend fun deleteFolderItems(folderId:String)

    @Query("UPDATE saved_item_table SET folderId = :destFolderId WHERE id = :itemId")
    suspend fun moveItemFolder(itemId:String, destFolderId:String)
}