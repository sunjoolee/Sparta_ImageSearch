package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.source.local.folder.FolderId
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
    val folderId: String = FolderId.NO_FOLDER.id
)
