package com.sparta.imagesearch.data

import androidx.annotation.ColorInt
import java.util.UUID

data class Folder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ColorInt val color: Int
)
