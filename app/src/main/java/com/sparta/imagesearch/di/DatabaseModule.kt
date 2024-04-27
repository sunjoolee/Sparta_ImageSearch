package com.sparta.imagesearch.di

import android.content.Context
import androidx.room.Room
import com.sparta.imagesearch.data.source.local.savedItem.SavedItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideSavedItemDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, SavedItemDatabase::class.java, "saved_item_database"
        ).build()
}