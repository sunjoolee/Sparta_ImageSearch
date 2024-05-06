package com.sparta.imagesearch.data.mappers

import com.sparta.imagesearch.data.source.local.savedItem.SavedItemEntity
import com.sparta.imagesearch.domain.Item

fun Item.toSavedItemEntity() = SavedItemEntity(
    id = 0,
    imageUrl = imageUrl,
    itemType = itemType,
    source = source,
    time = time,
    folderId = folderId
)

fun SavedItemEntity.toItem() = Item(
    imageUrl = imageUrl,
    itemType = itemType,
    source = source,
    time = time
)