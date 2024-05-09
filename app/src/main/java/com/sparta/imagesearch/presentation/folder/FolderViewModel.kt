package com.sparta.imagesearch.presentation.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.combine
import com.sparta.imagesearch.data.mappers.toFolder
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FolderScreenState(
    val folders: List<Folder> = emptyList(),
    val selectedFolderId: Int = FolderId.DEFAULT_FOLDER.id,
    val itemsInFolder: List<Item> = emptyList(),
    val showAddDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val targetItem: Item? = null,
    val showMoveDialog: Boolean = false,
)

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository,
    private val folderRepository: FolderRepository
) : ViewModel(), FolderScreenInputs {
    private val TAG = "FolderViewModel"

    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    private val _selectedFolderId = MutableStateFlow(DefaultFolder.id)
    private val _itemsInFolder = MutableStateFlow<List<Item>>(emptyList())
    private val _showAddDialog = MutableStateFlow<Boolean>(false)
    private val _showDeleteDialog = MutableStateFlow<Boolean>(false)
    private val _targetItem = MutableStateFlow<Item?>(null)
    private val _showMoveDialog = MutableStateFlow<Boolean>(false)

    private val _state = MutableStateFlow(FolderScreenState())
    val state = _state.asStateFlow()

    val inputs = this@FolderViewModel

    init {
        viewModelScope.launch {
            folderRepository.getAllFolders().collect { folders ->
                if (folders.find { it.id == FolderId.DEFAULT_FOLDER.id } == null) {
                    _folders.value = listOf(DefaultFolder.toFolder())
                }
                _folders.value += folders
            }
        }
        viewModelScope.launch {
            _selectedFolderId.collect { selectedFolderId ->
                savedItemRepository.getSavedItemsByFolderId(selectedFolderId).collect {
                    _itemsInFolder.value = it
                }
            }
        }

        viewModelScope.launch {
            combine(
                _folders,
                _selectedFolderId,
                _itemsInFolder,
                _showAddDialog,
                _showDeleteDialog,
                _showMoveDialog
            ) { folders, selectedFolderId, itemsInFolder,
                showAddDialog, showDeleteDialog, showMoveDialog ->
                FolderScreenState(
                    folders = folders,
                    selectedFolderId = selectedFolderId,
                    itemsInFolder = itemsInFolder,
                    showAddDialog = showAddDialog,
                    showDeleteDialog = showDeleteDialog,
                    showMoveDialog = showMoveDialog
                )
            }.collect {
                _state.value = it
            }
        }
    }

    override fun selectFolder(folder: Folder) {
        _selectedFolderId.value = folder.id
    }

    override fun unSaveItem(item: Item) {
        viewModelScope.launch {
            savedItemRepository.deleteSavedItem(item)
        }
        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
    }

    override fun toggleShowAddDialog() {
        _showAddDialog.value = !_showAddDialog.value
    }

    override fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        viewModelScope.launch {
            folderRepository.upsertFolder(newFolder)
        }
    }

    override fun toggleDeleteDialog() {
        _showDeleteDialog.value = !_showDeleteDialog.value
    }

    override fun deleteFolders(deleteFolderIdList: List<Int>) {
        if (deleteFolderIdList.contains(_selectedFolderId.value))
            _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id

        _folders.value = _folders.value.filterNot {
            deleteFolderIdList.contains(it.id)
        }
        viewModelScope.launch {
            folderRepository.deleteFoldersById(deleteFolderIdList)
            savedItemRepository.deleteSavedItemsByFolderId(deleteFolderIdList)
        }
    }

    override fun toggleMoveDialog(targetItem: Item?) {
        if (targetItem == null) return

        if (!_showMoveDialog.value) {
            _showMoveDialog.value = true
            _targetItem.value = targetItem
        } else {
            _showMoveDialog.value = false
            _targetItem.value = null
        }
    }

    override fun moveFolder(destFolderId: Int) {
        if (_targetItem.value == null) return

        val targetItem = _targetItem.value!!
        if (destFolderId == targetItem.folderId) return

        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == targetItem.imageUrl }
        viewModelScope.launch {
            savedItemRepository.moveSavedItem(targetItem.imageUrl, destFolderId)
        }
    }

}
