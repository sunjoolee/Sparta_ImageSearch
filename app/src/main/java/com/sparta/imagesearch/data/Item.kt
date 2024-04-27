package com.sparta.imagesearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.squareup.moshi.JsonClass
import java.util.UUID

enum class ItemType {
    IMAGE_TYPE,
    VIDEO_TYPE,
    UNKNOWN
}


@JsonClass(generateAdapter = true)
@Entity(tableName = "saved_item_table")
data class Item(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val itemType: ItemType,
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: String = FolderId.NO_FOLDER.id
)
