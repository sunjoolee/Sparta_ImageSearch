package com.sparta.imagesearch.domain.usecase

import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.KakaoSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSearchItemsUsecase @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository
) {
    suspend operator fun invoke(query: String, page: Int): Flow<List<Item>> {
        val imageResponseFlow =
            kakaoSearchRepository.getImages(query = query, page = page)
        val videoResponseFlow =
            kakaoSearchRepository.getVideos(query = query, page = page)

        return combine(imageResponseFlow, videoResponseFlow) { i, v ->
            val itemList = mutableListOf<Item>()
            if (i is ApiResponse.Success) {
                itemList.addAll(i.data.documents?.map { it.toItem() } ?: emptyList())
            }
            if (v is ApiResponse.Success) {
                itemList.addAll(v.data.documents?.map { it.toItem() } ?: emptyList())
            }
            itemList.toList().distinctBy { it.imageUrl }
        }
    }
}