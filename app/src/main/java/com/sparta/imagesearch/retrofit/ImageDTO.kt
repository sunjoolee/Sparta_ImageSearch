package com.sparta.imagesearch.retrofit

import java.time.LocalDateTime
import java.util.Date

data class ImageResponse(
    val meta: Meta,
    val documents: MutableList<Document>?
)

data class Meta(
    val total_count:Int,
    val pageable_count:Int,
    val is_end:Boolean
)

data class  Document(
    val collection:String,
    val thumbnail_url:String,
    val image_url:String,
    val width:Int,
    val height:Int,
    val display_sitename:String,
    val doc_url:String,
    val datetime: String
)