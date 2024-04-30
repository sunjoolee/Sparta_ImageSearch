package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sparta.imagesearch.entity.Item


@Database(entities = [Item::class], version = 1)
abstract class SavedItemDatabase(): RoomDatabase(){
    abstract fun getSavedItemDAO(): SavedItemDAO
}