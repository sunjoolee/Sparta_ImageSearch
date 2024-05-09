package com.sparta.imagesearch.data.mappers

import com.sparta.imagesearch.data.source.local.savedItem.SavedItem
import com.sparta.imagesearch.data.source.remote.Document
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.util.formatDate

fun Document.toItem(): Item =
    when (this) {
        is Document.ImageDocument -> Item(
            imageUrl = imageUrl,
            source = displaySitename,
            time = datetime.formatDate()
        )

        is Document.VideoDocument -> Item(
            imageUrl = thumbnail,
            source = author,
            time = datetime.formatDate()
        )
    }

fun Item.toSavedItem(): SavedItem = SavedItem(
    imageUrl = imageUrl,
    source = source,
    time = time
)

fun SavedItem.toItem(): Item = Item(
    imageUrl = imageUrl,
    source = source,
    time = time
)

