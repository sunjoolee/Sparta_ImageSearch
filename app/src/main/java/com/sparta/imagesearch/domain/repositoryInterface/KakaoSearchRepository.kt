package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.source.remote.KakaoDocument
import com.sparta.imagesearch.data.source.remote.KakaoSearchDTO
import kotlinx.coroutines.flow.Flow

interface KakaoSearchRepository {
    suspend fun getImages(query: String, page:Int, pageSize:Int):
            Flow<ApiResponse<KakaoSearchDTO<KakaoDocument.ImageKakaoDocument>>>
    suspend fun getVideos(query: String, page:Int, pageSize:Int):
            Flow<ApiResponse<KakaoSearchDTO<KakaoDocument.VideoKakaoDocument>>>
}