package com.sparta.imagesearch.domain

import com.sparta.imagesearch.data.source.local.folder.FolderId

data class Item(
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: String = FolderId.NO_FOLDER.id
)
