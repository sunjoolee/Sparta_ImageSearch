package com.sparta.imagesearch.data.mappers

import com.sparta.imagesearch.data.source.local.savedItem.SavedItem
import com.sparta.imagesearch.data.source.remote.Document
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.ItemType
import com.sparta.imagesearch.presentation.util.formatDate

fun Document.toItem(): Item =
    when (this) {
        is Document.ImageDocument -> Item(
            itemType = ItemType.IMAGE,
            imageUrl = imageUrl,
            source = displaySitename,
            time = datetime.formatDate()
        )

        is Document.VideoDocument -> Item(
            itemType = ItemType.VIDEO,
            imageUrl = thumbnail,
            source = author,
            time = datetime.formatDate()
        )
    }

fun Item.toSavedItem(): SavedItem = SavedItem(
    itemTypeCode = itemType.code,
    imageUrl = imageUrl,
    source = source,
    time = time,
    folderId = folderId
)

fun SavedItem.toItem(): Item = Item(
    itemType = if(itemTypeCode == ItemType.IMAGE.code) ItemType.IMAGE else ItemType.VIDEO,
    imageUrl = imageUrl,
    source = source,
    time = time,
    folderId = folderId
)

