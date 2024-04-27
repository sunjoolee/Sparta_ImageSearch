package com.sparta.imagesearch.data.source.remote.retrofit

import com.google.gson.annotations.SerializedName

data class VideoDTO(
    val meta: VideoMeta,
    val documents: MutableList<VideoDocument>?
)

data class VideoMeta(
    @SerializedName("total_count")
    val totalCount:Int,
    @SerializedName("pageable_count")
    val pageableCount:Int,
    @SerializedName("is_end")
    val isEnd:Boolean
)

data class VideoDocument(
    val title:String,
    val url:String,
    val datetime: String,
    @SerializedName("play_time")
    val playTime:Int,
    val thumbnail:String,
    val author:String
)