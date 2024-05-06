package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.domain.ItemType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "saved_item_table")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val imageUrl: String,
    val itemType: ItemType,
    val source: String,
    val time: String,
    val folderId: String = FolderId.NO_FOLDER.id
)
