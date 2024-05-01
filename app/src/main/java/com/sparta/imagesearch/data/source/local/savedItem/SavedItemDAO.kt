package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sparta.imagesearch.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("SELECT * FROM saved_item_table")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM saved_item_table WHERE folderId = :folderId")
    fun getFolderItems(folderId:String): Flow<List<Item>>

    @Query("DELETE FROM saved_item_table WHERE folderId = :folderId")
    fun deleteFolderItems(folderId:String)

    @Query("UPDATE saved_item_table SET folderId = :destFolderId WHERE id = :itemId")
    fun moveItemFolder(itemId:String, destFolderId:String)
}