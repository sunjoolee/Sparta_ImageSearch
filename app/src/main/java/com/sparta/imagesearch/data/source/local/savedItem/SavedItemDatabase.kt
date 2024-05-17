package com.sparta.imagesearch.data.source.local.savedItem

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sparta.imagesearch.domain.Item


@Database(entities = [SavedItem::class], version = 5)
abstract class SavedItemDatabase(): RoomDatabase(){
    abstract fun getSavedItemDAO(): SavedItemDAO
}