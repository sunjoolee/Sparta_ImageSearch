package com.sparta.imagesearch.data

import java.util.UUID

data class Folder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val colorId: Int
)
