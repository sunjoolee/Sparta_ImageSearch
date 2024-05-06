package com.sparta.imagesearch.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchApi {
    @GET("image")
    suspend fun getImageDTO(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") pageSize: Int = 10,
        @Query("sort") sort: String = "recency"
    ): KakaoSearchDTO<KakaoDocument.ImageKakaoDocument>

    @GET("vclip")
    suspend fun getVideoDTO(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") pageSize: Int = 10,
        @Query("sort") sort: String = "recency"
    ): KakaoSearchDTO<KakaoDocument.VideoKakaoDocument>
}