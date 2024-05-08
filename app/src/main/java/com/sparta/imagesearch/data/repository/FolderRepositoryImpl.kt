package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.mappers.toFolder
import com.sparta.imagesearch.data.mappers.toFolderEntity
import com.sparta.imagesearch.data.source.local.folder.FolderDatabase
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderSource: FolderDatabase
) : FolderRepository {
    private val folderDAO = folderSource.getFolderDAO()

    override fun upsertFolders(folders: List<Folder>) =
        folderDAO.upsertFolders(folders.map { it.toFolderEntity() })

    override fun getAllFolders(): Flow<List<Folder>> =
        merge(
            flowOf(listOf(DefaultFolder.toFolder())),
            folderDAO.getAllFolders().transform {
                it.map { folderEntity ->
                    folderEntity.toFolder()
                }
            }
        )
}