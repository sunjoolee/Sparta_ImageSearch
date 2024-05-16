package com.sparta.imagesearch.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchApi {
    @GET("image")
    suspend fun getImageDTO(
        @Query("query") query: String,
        @Query("size") size: Int = 5,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "accuracy"
    ): KakaoSearchDTO<Document.ImageDocument>

    @GET("vclip")
    suspend fun getVideoDTO(
        @Query("query") query: String,
        @Query("size") size: Int = 5,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "accuracy"
    ): KakaoSearchDTO<Document.VideoDocument>
}