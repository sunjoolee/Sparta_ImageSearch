package com.sparta.imagesearch.data.source.remote.retrofit

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoSearchApi {
    @Headers("Authorization: KakaoAK $KAKAO_REST_API_KEY")
    @GET("image")
    suspend fun getImageDTO(
        @Query("query") query: String,
        @Query("size") size: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "recency"
    ): KakaoSearchDTO<ImageDocument>

    @Headers("Authorization: KakaoAK $KAKAO_REST_API_KEY")
    @GET("vclip")
    suspend fun getVideoDTO(
        @Query("query") query: String,
        @Query("size") size: Int = 10,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "recency"
    ): KakaoSearchDTO<VideoDocument>
}