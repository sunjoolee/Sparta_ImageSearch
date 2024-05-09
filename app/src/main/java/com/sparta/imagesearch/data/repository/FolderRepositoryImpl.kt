package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.mappers.toFolder
import com.sparta.imagesearch.data.mappers.toFolderEntity
import com.sparta.imagesearch.data.source.local.folder.FolderDatabase
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderSource: FolderDatabase
) : FolderRepository {
    private val folderDAO = folderSource.getFolderDAO()
    override suspend fun upsertFolder(folder: Folder) = withContext(Dispatchers.IO) {
        folderDAO.upsertFolder(folder.toFolderEntity())
    }

    override suspend fun getAllFolders(): Flow<List<Folder>> = withContext(Dispatchers.IO) {
        folderDAO.getAllFolders().transform {
            emit(it.map { folderItem -> folderItem.toFolder() })
        }
    }

    override suspend fun getFolderById(folderId: Int): Flow<Folder> = withContext(Dispatchers.IO) {
        folderDAO.getFolderById(folderId).transform {
            emit(it.toFolder())
        }
    }

    override suspend fun deleteFoldersById(folderIds: List<Int>) = withContext(Dispatchers.IO) {
        folderIds.forEach { folderDAO.deleteFolderById(it) }
    }
}