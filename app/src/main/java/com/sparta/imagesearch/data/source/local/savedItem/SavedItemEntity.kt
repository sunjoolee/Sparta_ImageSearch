package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_item_table")
data class SavedItem(
    @PrimaryKey
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: Int = 0
)