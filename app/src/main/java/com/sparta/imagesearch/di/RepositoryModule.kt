package com.sparta.imagesearch.di

import com.sparta.imagesearch.data.repository.ItemRepositoryImpl
import com.sparta.imagesearch.domain.repositoryInterface.KakaoSearchRepository
import com.sparta.imagesearch.data.repository.KakaoSearchRepositoryImpl
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.data.repository.SavedItemRepositoryImpl
import com.sparta.imagesearch.domain.repositoryInterface.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsKakaoSearchRepository(repositoryImpl: KakaoSearchRepositoryImpl): KakaoSearchRepository
    @Binds
    abstract fun bindsItemRepository(repositoryImpl: ItemRepositoryImpl): ItemRepository
    @Binds
    abstract fun bindsSavedItemRepository(repositoryImpl: SavedItemRepositoryImpl): SavedItemRepository
}