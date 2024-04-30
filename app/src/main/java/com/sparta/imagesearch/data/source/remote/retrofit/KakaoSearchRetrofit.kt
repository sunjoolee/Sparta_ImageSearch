package com.sparta.imagesearch.data.source.remote.retrofit

import com.sparta.imagesearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit
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
            .addInterceptor(KakaoInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    val kakaoSearchApi = kakaoSearchRetrofit.create(KakaoSearchApi::class.java)
}

class KakaoInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val auth = BuildConfig.KAKAO_REST_API_KEY

        builder.addHeader("Authorization", "KakaoAK $auth")
        return chain.proceed(builder.build())
    }
}