package com.sparta.imagesearch.di

import com.sparta.imagesearch.data.source.remote.retrofit.KakaoSearchApi
import com.sparta.imagesearch.data.source.remote.retrofit.KakaoSearchRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteSourceModule {
    @Singleton
    @Provides
    fun provideKakaoSearchApi():KakaoSearchApi = KakaoSearchRetrofit.kakaoSearchApi
}