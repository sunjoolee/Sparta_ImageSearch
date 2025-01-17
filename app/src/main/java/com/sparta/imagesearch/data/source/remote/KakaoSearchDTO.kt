package com.sparta.imagesearch.data.source.remote

import com.google.gson.annotations.SerializedName

data class KakaoSearchDTO<T:Document>(
    val meta: Meta,
    val documents: List<T>?
)

data class Meta(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)

sealed interface Document {

    data class ImageDocument(
        val collection: String,
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,
        @SerializedName("image_url")
        val imageUrl: String,
        val width: Int,
        val height: Int,
        @SerializedName("display_sitename")
        val displaySitename: String,
        @SerializedName("doc_url")
        val docUrl: String,
        val datetime: String
    ):Document

    data class VideoDocument(
        val title: String,
        val url: String,
        val datetime: String,
        @SerializedName("play_time")
        val playTime: Int,
        val thumbnail: String,
        val author: String
    ):Document
}
