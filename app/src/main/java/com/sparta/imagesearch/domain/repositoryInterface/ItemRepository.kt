package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.source.remote.ImageDocument
import com.sparta.imagesearch.data.source.remote.KakaoSearchDTO
import com.sparta.imagesearch.data.source.remote.VideoDocument
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun getImages(query: String): Flow<ApiResponse<KakaoSearchDTO<ImageDocument>>>
    suspend fun getVideos(query: String): Flow<ApiResponse<KakaoSearchDTO<VideoDocument>>>
}