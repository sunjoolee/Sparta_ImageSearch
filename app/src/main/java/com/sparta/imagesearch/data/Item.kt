package com.sparta.imagesearch.data

enum class ItemType {
    Image,
    Video
}

open class Item(
    val type: ItemType,
    open var folder: ImageFolder? = null,
    open val time: String
) {
}