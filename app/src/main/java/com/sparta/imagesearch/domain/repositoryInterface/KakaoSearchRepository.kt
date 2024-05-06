package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.source.remote.Document
import com.sparta.imagesearch.data.source.remote.KakaoSearchDTO
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepository {
    suspend fun getImages(query: String): Flow<ApiResponse<KakaoSearchDTO<Document.ImageDocument>>>
    suspend fun getVideos(query: String): Flow<ApiResponse<KakaoSearchDTO<Document.VideoDocument>>>
}