package com.sparta.imagesearch.domain

import com.sparta.imagesearch.data.source.local.folder.FolderId
import java.util.UUID


data class Item(
    val itemType: ItemType,
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: String = FolderId.NO_FOLDER.id
)
