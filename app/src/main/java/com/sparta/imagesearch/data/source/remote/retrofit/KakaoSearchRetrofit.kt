package com.sparta.imagesearch.data.source.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val KAKAO_REST_API_KEY = "664eaa84ddf5f3df2af539d94a1c9ae9"
object KakaoSearchRetrofit {
    private const val SEARCH_BASE_URL = "https://dapi.kakao.com/v2/search/"

    private val kakaoSearchRetrofit = Retrofit.Builder()
        .baseUrl(SEARCH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()
    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    val kakaoSearchApi = kakaoSearchRetrofit.create(KakaoSearchApi::class.java)
}
