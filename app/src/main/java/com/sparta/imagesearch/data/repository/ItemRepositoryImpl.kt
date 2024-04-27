package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.source.remote.retrofit.ImageDocument
import com.sparta.imagesearch.data.source.remote.retrofit.VideoDocument
import com.sparta.imagesearch.data.source.remote.retrofit.KakaoSearchApi
import com.sparta.imagesearch.util.formatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val kakaoSearchSource: KakaoSearchApi
): ItemRepository {
    override suspend fun getItems(query: String): List<Item> {
        if (query.isBlank()) return emptyList()
        val imageItems = getImages(query).map { it.convert() }
        val videoItems = getVideos(query).map { it.convert() }

        val items = mutableListOf<Item>()
        items.run {
            addAll(imageItems)
            addAll(videoItems)
            sortWith { item1, item2 -> item1.time.compareTo(item2.time) }
            reverse()
        }
        return items
    }

    private suspend fun getImages(query: String): List<ImageDocument> {
        val images = mutableListOf<ImageDocument>()
        val imageDTO = CoroutineScope(Dispatchers.Default).async {
            try {
                kakaoSearchSource.getImageDTO(query)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }.await()
        imageDTO?.documents?.forEach { images.add(it) }
        return images
    }

    suspend fun getVideos(query: String): List<VideoDocument> {
        val videos = mutableListOf<VideoDocument>()
        val videoDTO = CoroutineScope(Dispatchers.Default).async {
            try {
                kakaoSearchSource.getVideoDTO(query)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }.await()
        videoDTO?.documents?.forEach { videos.add(it) }
        return videos
    }

    private fun ImageDocument.convert() =
        Item(
            itemType = ItemType.IMAGE_TYPE,
            imageUrl = imageUrl,
            source = displaySitename,
            time = datetime.formatDate()
        )

    private fun VideoDocument.convert() =
        Item(
            itemType = ItemType.VIDEO_TYPE,
            imageUrl = thumbnail,
            source = author,
            time = datetime.formatDate()
        )
}