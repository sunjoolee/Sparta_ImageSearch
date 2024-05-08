package com.sparta.imagesearch.data.source.local.folder

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FolderEntity::class], version = 1)
abstract class FolderDatabase(): RoomDatabase(){
    abstract fun getFolderDAO(): FolderDAO
}