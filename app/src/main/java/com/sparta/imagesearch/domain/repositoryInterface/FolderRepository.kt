package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun upsertFolders(folders: List<Folder>)
    fun getAllFolders(): Flow<List<Folder>>
}