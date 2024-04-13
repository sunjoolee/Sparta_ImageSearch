package com.sparta.imagesearch.data

import com.squareup.moshi.JsonClass
import java.util.UUID

enum class ItemType {
    IMAGE_TYPE,
    VIDEO_TYPE,
    UNKNOWN
}

@JsonClass(generateAdapter = true)
data class Item(
    val id: String = UUID.randomUUID().toString(),
    val itemType: ItemType,
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: String = Folder.NO_FOLDER_ID
)
