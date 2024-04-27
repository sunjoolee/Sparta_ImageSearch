package com.sparta.imagesearch.di

import com.sparta.imagesearch.data.repository.ItemRepository
import com.sparta.imagesearch.data.repository.ItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsItemRepository(repositoryImpl: ItemRepositoryImpl): ItemRepository
}