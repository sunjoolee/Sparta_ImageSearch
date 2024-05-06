package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedItemEntity::class], version = 2)
abstract class SavedItemDatabase(): RoomDatabase(){
    abstract fun getSavedItemDAO(): SavedItemDAO
}