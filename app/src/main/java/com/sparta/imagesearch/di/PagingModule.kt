package com.sparta.imagesearch.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sparta.imagesearch.data.repository.KakaoSearchRepositoryImpl
import com.sparta.imagesearch.data.source.local.item.ItemDatabase
import com.sparta.imagesearch.data.source.local.item.ItemEntity
import com.sparta.imagesearch.data.source.remote.ItemRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {
    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideItemPager(itemDatabase: ItemDatabase, kakaoSearchRepositoryImpl: KakaoSearchRepositoryImpl):
            Pager<Int, ItemEntity> = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = ItemRemoteMediator(
            itemDatabase = itemDatabase,
            kakaoSearchRepositoryImpl = kakaoSearchRepositoryImpl
        ),
        pagingSourceFactory = {
            itemDatabase.getItemDAO().getAllItemsPagingSource()
        }
    )
}