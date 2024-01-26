package com.sparta.imagesearch.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val REST_API_KEY = "664eaa84ddf5f3df2af539d94a1c9ae9"

interface SearchNetWorkInterface {
    @Headers("Authorization: KakaoAK ${REST_API_KEY}")
    @GET("image")
    suspend fun getImage(
        @Query("query") query:String,
        @Query("size") size:Int = 80
    ) : ImageResponse
}
