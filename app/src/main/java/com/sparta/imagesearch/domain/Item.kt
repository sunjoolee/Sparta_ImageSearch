package com.sparta.imagesearch.domain

data class Item(
    val imageUrl: String,
    val source: String,
    val time: String,
    val heartColorHex: String = FolderColor.NO_COLOR.colorHex,
    val folderId: Int = FolderId.NO_FOLDER.id
)
