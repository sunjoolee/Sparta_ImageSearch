package com.sparta.imagesearch.presentation.folder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.combine
import com.sparta.imagesearch.data.mappers.toFolder
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.domain.usecase.GetFoldersUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FolderScreenState(
    val folders: List<Folder> = emptyList(),
    val selectedFolderId: Int = FolderId.DEFAULT_FOLDER.id,
    val selectedFolderColorHex: String = FolderColor.COLOR1.colorHex,
    val selectedFolderItems:List<Item> = emptyList(),
    val showAddDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showMoveDialog: Boolean = false,
    val targetItem: Item? = null
)

interface FolderScreenInputs {
    fun selectFolder(folder: Folder)
    fun unSaveItem(item: Item)
    fun openAddDialog()
    fun closeAddDialog()
    fun openDeleteDialog()
    fun closeDeleteDialog()
    fun openMoveDialog(targetItem: Item)
    fun closeMoveDialog()
}

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository,
    private val getFoldersUsecase: GetFoldersUsecase
) : ViewModel(), FolderScreenInputs {
    private val TAG = this::class.java.simpleName

    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    private val _selectedFolderId = MutableStateFlow(FolderId.DEFAULT_FOLDER.id)
    private val _showAddDialog = MutableStateFlow(false)
    private val _showDeleteDialog = MutableStateFlow(false)
    private val _showMoveDialog = MutableStateFlow(false)
    private val _targetItem = MutableStateFlow<Item?>(null)

    private val _state = MutableStateFlow(FolderScreenState())
    val state = _state.asStateFlow()

    val inputs = this@FolderViewModel

    init {
        viewModelScope.launch {
            getFoldersUsecase.invoke().collect { folders ->
                _folders.update {
                    if (folders.find { it.id == FolderId.DEFAULT_FOLDER.id } == null)
                        listOf(DefaultFolder.toFolder()) + folders
                    else folders
                }
            }
        }

        combine(
            _folders,
            _selectedFolderId,
            _showAddDialog,
            _showDeleteDialog,
            _showMoveDialog,
            _targetItem
        ) { folders, selectedFolderId,
            showAddDialog, showDeleteDialog, showMoveDialog, targetItem->
            _state.value = FolderScreenState(
                folders = folders,
                selectedFolderId = selectedFolderId,
                selectedFolderColorHex = folders.find { it.id == selectedFolderId }?.colorHex
                    ?: FolderColor.COLOR1.colorHex,
                selectedFolderItems = folders.find { it.id == selectedFolderId }?.items
                    ?: emptyList(),
                showAddDialog = showAddDialog,
                showDeleteDialog = showDeleteDialog,
                showMoveDialog = showMoveDialog,
                targetItem = targetItem
            )
        }.launchIn(viewModelScope)
    }

    override fun selectFolder(folder: Folder) {
        Log.d(TAG, "selectFolder) selectedFolderId: ${folder.id}")
        _selectedFolderId.value = folder.id
    }

    override fun unSaveItem(item: Item) {
        viewModelScope.launch {
            savedItemRepository.deleteSavedItem(item)
        }
    }

    override fun openAddDialog(){
        _showAddDialog.value = true
    }
    override fun closeAddDialog() {
        _showAddDialog.value = false
    }
    override fun openDeleteDialog() {
        _showDeleteDialog.value = true
    }

    override fun closeDeleteDialog() {
        _showDeleteDialog.update {
            if (it) _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id
            false
        }
    }

    override fun openMoveDialog(targetItem: Item) {
        _showMoveDialog.value = true
        _targetItem.value = targetItem
    }

    override fun closeMoveDialog() {
        _showMoveDialog.value = false
        _targetItem.value = null
    }
}
