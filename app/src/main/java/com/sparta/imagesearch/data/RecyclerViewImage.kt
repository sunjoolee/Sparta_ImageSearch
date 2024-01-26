package com.sparta.imagesearch.data

import android.util.Log
import com.sparta.imagesearch.retrofit.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class RecyclerViewImage(
    val folder: ImageFolder? = null,
    val thumbnailUrl: String,
    val source: String,
    val time: String
) {

    companion object {
        fun createFromImageDocument(imageDocument: Document): RecyclerViewImage {
            val newRecyclerViewImage = RecyclerViewImage(
                thumbnailUrl = imageDocument.thumbnail_url,
                source = imageDocument.display_sitename,
                time = LocalDateTime
                    .parse(imageDocument.datetime, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            Log.d("RecyclerViewImage", "${newRecyclerViewImage.toString()}")
            return newRecyclerViewImage
        }
    }
}