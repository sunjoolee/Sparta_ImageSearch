package com.sparta.imagesearch.domain

data class Folder(
    val id: Int = FolderId.DEFAULT_FOLDER.id,
    val name: String,
    val colorHex: String,
    val items: List<Item> = emptyList()
)


