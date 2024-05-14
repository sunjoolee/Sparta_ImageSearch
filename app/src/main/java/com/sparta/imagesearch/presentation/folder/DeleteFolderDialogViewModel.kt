package com.sparta.imagesearch.presentation.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeleteFolderDialogState(
    val folders: List<Folder> = emptyList(),
    val deleteFoldersId: List<Int> = emptyList(),
    val canDeleteFolders: Boolean = false
)

interface DeleteFolderDialogInputs {
    fun selectDeleteFolderId(folderId: Int)
    fun clearDeleteFoldersId()
    fun deleteFolders()
}

@HiltViewModel
class DeleteFolderDialogViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val savedItemRepository: SavedItemRepository
) : ViewModel(), DeleteFolderDialogInputs {
    private val TAG = this::class.java.simpleName

    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    private val _deleteFoldersId = MutableStateFlow<List<Int>>(emptyList())
    private val _confirmButtonEnabled = MutableStateFlow(false)

    private val _state = MutableStateFlow(DeleteFolderDialogState())
    val state = _state.asStateFlow()

    val inputs = this@DeleteFolderDialogViewModel

    init {
        viewModelScope.launch {
            folderRepository.getAllFolders().collect {
                _folders.value = it
            }
        }

        viewModelScope.launch {
            _deleteFoldersId.collect {
                _confirmButtonEnabled.value = it.isNotEmpty()
            }
        }

        combine(
            _folders,
            _deleteFoldersId,
            _confirmButtonEnabled
        ) { folders, deleteFoldersId, confirmButtonEnabled ->
            _state.update {
                DeleteFolderDialogState(
                    folders,
                    deleteFoldersId,
                    confirmButtonEnabled
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun selectDeleteFolderId(folderId: Int) {
        _deleteFoldersId.update {
            if (it.contains(folderId)) it.filter { id -> id != folderId }
            else it + listOf(folderId)
        }
    }

    override fun clearDeleteFoldersId() {
        _deleteFoldersId.update { emptyList() }
    }

    override fun deleteFolders() {
        viewModelScope.launch {
            folderRepository.deleteFoldersById(_deleteFoldersId.value)
            savedItemRepository.deleteSavedItemsByFolderId(_deleteFoldersId.value)
        }
    }
}