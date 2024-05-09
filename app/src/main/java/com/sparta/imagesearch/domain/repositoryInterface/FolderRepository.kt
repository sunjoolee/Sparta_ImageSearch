package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.domain.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    suspend fun upsertFolder(folder: Folder)
    suspend fun getAllFolders(): Flow<List<Folder>>
    suspend fun getFolderById(folderId: Int): Flow<Folder>
    suspend fun deleteFoldersById(folderIds:List<Int>)
}