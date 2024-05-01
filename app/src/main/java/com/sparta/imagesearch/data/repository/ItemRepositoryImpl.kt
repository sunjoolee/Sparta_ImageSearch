package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.source.remote.Document
import com.sparta.imagesearch.data.source.remote.KakaoSearchApi
import com.sparta.imagesearch.data.source.remote.KakaoSearchDTO
import com.sparta.imagesearch.domain.repositoryInterface.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val kakaoSearchSource: KakaoSearchApi
) : ItemRepository {
    override suspend fun getImages(query: String): Flow<ApiResponse<KakaoSearchDTO<Document.ImageDocument>>> =
        handleKakaoSearchDTO {
            kakaoSearchSource.getImageDTO(query)
        }

    override suspend fun getVideos(query: String): Flow<ApiResponse<KakaoSearchDTO<Document.VideoDocument>>> =
        handleKakaoSearchDTO {
            kakaoSearchSource.getVideoDTO(query)
        }

    private fun <T:Document> handleKakaoSearchDTO(
        execute: suspend () -> KakaoSearchDTO<T>
    )
            : Flow<ApiResponse<KakaoSearchDTO<T>>> = flow {
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(execute()))
        } catch (e: HttpException) {
            emit(ApiResponse.Fail.Error(e.code(), e.message()))
        } catch (e: Exception) {
            emit(ApiResponse.Fail.Exception(e))
        }
    }
}