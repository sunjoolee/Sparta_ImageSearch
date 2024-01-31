package com.sparta.imagesearch.data

import android.util.Log
import com.sparta.imagesearch.retrofit.VideoDocument
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Video(
    var folder: ImageFolder? = null,
    val thumbnail: String,
    override val time: String
):Item(ItemType.Video, time) {
    companion object {
        fun createFromVideoDocument(videoDocument: VideoDocument): Video {
            val newVideo = Video(
                thumbnail = videoDocument.thumbnail,
                time = LocalDateTime
                    .parse(videoDocument.datetime, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            //Log.d("Video", "${newVideo.toString()}")
            return newVideo
        }
    }
}
