package com.sparta.imagesearch.presentation.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
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

data class MoveFolderDialogState(
    val targetFolderId: Int = FolderId.DEFAULT_FOLDER.id,
    val folders: List<Folder> = emptyList(),
    val enableConfirmButton: Boolean = false
)

interface MoveFolderDialogInputs {
    fun setTargetItem(targetItem: Item)
    fun selectTargetFolderId(targetFolderId: Int)
    fun moveFolder()
}

@HiltViewModel
class MoveFolderDialogViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val savedItemRepository: SavedItemRepository
) : ViewModel(), MoveFolderDialogInputs {

    private val _targetItem = MutableStateFlow<Item?>(null)
    private val _targetFolderId = MutableStateFlow(FolderId.DEFAULT_FOLDER.id)
    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    private val _enableConfirmButton = MutableStateFlow(false)

    private val _state = MutableStateFlow(MoveFolderDialogState())
    val state = _state.asStateFlow()

    val inputs = this@MoveFolderDialogViewModel

    init {
        viewModelScope.launch {
            folderRepository.getAllFolders().collect {
                _folders.value = it
            }
        }

        combine(_targetItem, _targetFolderId) { targetItem, targetFolderId ->
            _enableConfirmButton.update {
                if (targetItem == null) false
                else targetItem.folderId != targetFolderId
            }
        }.launchIn(viewModelScope)

        combine(
            _targetFolderId,
            _folders,
            _enableConfirmButton
        ) { targetFolderId, folders, enableConfirmButton ->
            _state.value = MoveFolderDialogState(targetFolderId, folders, enableConfirmButton)
        }.launchIn(viewModelScope)
    }

    override fun setTargetItem(targetItem: Item) {
        _targetItem.value = targetItem
    }

    override fun selectTargetFolderId(targetFolderId: Int) {
        _targetFolderId.value = targetFolderId
    }

    override fun moveFolder() {
        viewModelScope.launch {
            savedItemRepository.moveSavedItem(_targetItem.value!!.imageUrl, _targetFolderId.value)
        }
    }
}