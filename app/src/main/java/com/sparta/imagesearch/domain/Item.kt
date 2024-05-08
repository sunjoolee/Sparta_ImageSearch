package com.sparta.imagesearch.domain

data class Item(
    val imageUrl: String,
    val source: String,
    val time: String,
    val folderId: Int = 0
)
