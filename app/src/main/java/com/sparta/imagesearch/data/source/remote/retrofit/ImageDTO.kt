package com.sparta.imagesearch.data.source.remote.retrofit

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    val imageMeta: ImageMeta,
    val documents: MutableList<ImageDocument>?
)

data class ImageMeta(
    @SerializedName("total_count")
    val totalCount:Int,
    @SerializedName("pageable_count")
    val pageableCount:Int,
    @SerializedName("is_end")
    val isEnd:Boolean
)

data class  ImageDocument(
    val collection:String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl:String,
    @SerializedName("image_url")
    val imageUrl:String,
    val width:Int,
    val height:Int,
    @SerializedName("display_sitename")
    val displaySitename:String,
    @SerializedName("doc_url")
    val docUrl:String,
    val datetime: String
)