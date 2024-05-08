package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun upsertFolder(folder: Folder)
    suspend fun upsertFolders(folders: List<Folder>)
    fun getAllFolders(): Flow<List<Folder>>

    fun getFolderById(folderId: Int): Flow<Folder>

    suspend fun deleteFoldersById(folderIds:List<Int>)
}