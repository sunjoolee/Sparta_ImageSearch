package com.sparta.imagesearch.data.source.local.item

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ItemDAO {
    @Upsert
    suspend fun upsertAllItems(items: List<ItemEntity>)

    @Query("SELECT * FROM item_table")
    fun getAllItemsPagingSource(): PagingSource<Int, ItemEntity>

    @Query("DELETE FROM item_table")
    suspend fun deleteAllItems()
}