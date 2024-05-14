package com.sparta.imagesearch.domain.usecase

import com.sparta.imagesearch.data.mappers.toFolder
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetFoldersUsecase @Inject constructor(
    private val foldersRepository: FolderRepository,
    private val savedItemRepository: SavedItemRepository
) {
    suspend operator fun invoke(): Flow<List<Folder>> {
        val foldersFlow = foldersRepository.getAllFolders()
            .transform {
                emit(
                    if (it.find { folder -> folder.id == FolderId.DEFAULT_FOLDER.id } == null)
                        listOf(DefaultFolder.toFolder()) + it
                    else
                        it
                )
            }
        val savedItemsFlow = savedItemRepository.getAllSavedItems()

        return combine(foldersFlow, savedItemsFlow) { folders, savedItems ->
            folders.map { folder ->
                folder.copy(items = savedItems.filter { item -> item.folderId == folder.id })
            }
        }
    }
}