package com.sparta.imagesearch.di

import com.sparta.imagesearch.data.repository.FolderRepositoryImpl
import com.sparta.imagesearch.data.repository.KakaoSearchRepositoryImpl
import com.sparta.imagesearch.data.repository.KeywordRepositoryImpl
import com.sparta.imagesearch.data.repository.SavedItemRepositoryImpl
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import com.sparta.imagesearch.domain.repositoryInterface.KakaoSearchRepository
import com.sparta.imagesearch.domain.repositoryInterface.KeywordRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsItemRepository(repositoryImpl: KakaoSearchRepositoryImpl): KakaoSearchRepository
    @Binds
    abstract fun bindsSavedItemRepository(repositoryImpl: SavedItemRepositoryImpl): SavedItemRepository
    @Binds
    abstract fun bindsFolderRepository(repositoryImpl: FolderRepositoryImpl): FolderRepository
    @Binds
    abstract fun bindsKeywordRepository(repositoryImpl: KeywordRepositoryImpl): KeywordRepository
}