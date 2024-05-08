package com.sparta.imagesearch.data.source.local.folder

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDAO {

    @Upsert
    fun upsertFolder(folder: FolderEntity)

    @Upsert
    suspend fun upsertFolders(folders: List<FolderEntity>)

    @Query("SELECT * FROM folder_table")
    fun getAllFolders(): Flow<List<FolderEntity>>
}