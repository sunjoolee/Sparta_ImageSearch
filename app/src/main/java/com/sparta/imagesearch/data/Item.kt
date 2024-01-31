package com.sparta.imagesearch.data

enum class ItemType{
    Image,
    Video
}

open class Item (val type:ItemType, open val time:String){
}