package com.sparta.imagesearch.data

import android.util.Log
import com.sparta.imagesearch.retrofit.ImageDocument
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Image(
    var folder: ImageFolder? = null,
    val imageUrl: String,
    val source: String,
    override val time: String
):Item(ItemType.Image, time) {

    companion object {
        fun createFromImageDocument(imageDocument: ImageDocument): Image {
            val newImage = Image(
                imageUrl = imageDocument.image_url,
                source = imageDocument.display_sitename,
                time = LocalDateTime
                    .parse(imageDocument.datetime, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            //Log.d("Image", "${newImage.toString()}")
            return newImage
        }
    }
}