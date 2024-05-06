package com.sparta.imagesearch.data.mappers

import com.sparta.imagesearch.data.source.local.item.ItemEntity
import com.sparta.imagesearch.data.source.remote.KakaoDocument
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.ItemType
import com.sparta.imagesearch.util.formatDate
import com.sparta.imagesearch.util.getCurrentDateTime

fun KakaoDocument.toItemEntity(keyword:String): ItemEntity = when (this) {
    is KakaoDocument.ImageKakaoDocument -> {
        ItemEntity(
            id = 0, //default id, meaning id is not yet set
            imageUrl = imageUrl,
            itemType = ItemType.IMAGE_TYPE,
            source = displaySitename,
            time = datetime.formatDate(),
            searchKeyword = keyword,
            searchTime = getCurrentDateTime()
        )
    }

    is KakaoDocument.VideoKakaoDocument -> {
        ItemEntity(
            id = 0, //default id, meaning id is not yet set
            itemType = ItemType.VIDEO_TYPE,
            imageUrl = thumbnail,
            source = author,
            time = datetime.formatDate(),
            searchKeyword = keyword,
            searchTime = getCurrentDateTime()
        )
    }
}


fun ItemEntity.toItem() = Item(
    itemType = itemType,
    imageUrl = imageUrl,
    source = source,
    time = time
)
