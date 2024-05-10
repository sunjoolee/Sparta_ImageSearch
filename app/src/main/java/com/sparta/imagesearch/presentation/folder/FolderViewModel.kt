package com.sparta.imagesearch.presentation.folder

import android.util.Log
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FolderScreenState(
    val folders: List<Folder> = emptyList(),
    val selectedFolderId: Int = FolderId.DEFAULT_FOLDER.id,
    val savedItemsInFolder: List<Item> = emptyList(),
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
    private val _selectedFolderId = MutableStateFlow(FolderId.DEFAULT_FOLDER.id)
    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val _savedItemsInFolder = MutableStateFlow<List<Item>>(emptyList())
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
                Log.d(TAG, "collect folders) folders.size: ${folders.size}")
                _folders.update {
                    if (folders.find { it.id == FolderId.DEFAULT_FOLDER.id } == null)
                        listOf(DefaultFolder.toFolder()) + folders
                    else folders
                }
            }
        }
        viewModelScope.launch {
            savedItemRepository.getAllSavedItems().collect {
                Log.d(TAG, "collect savedItems) savedItems.size: ${it.size}")
                _savedItems.value = it
            }
        }

        combine(_selectedFolderId, _savedItems) { selectedFolderId, savedItems ->
            Log.d(TAG, "combine savedItemsInFolder) selectedFolderId: $selectedFolderId, savedItems: ${savedItems}")
            _savedItemsInFolder.update {
                savedItems.filter { it.folderId == selectedFolderId }
            }
            Log.d(TAG, "combine savedItemsInFolder) savedItemsInFolder: ${_savedItemsInFolder.value}")
        }.launchIn(viewModelScope)

        combine(
            _folders,
            _selectedFolderId,
            _savedItemsInFolder,
            _showAddDialog,
            _showDeleteDialog,
            _showMoveDialog
        ) { folders, selectedFolderId, itemsInFolder,
            showAddDialog, showDeleteDialog, showMoveDialog ->
            _state.value = FolderScreenState(
                folders = folders,
                selectedFolderId = selectedFolderId,
                savedItemsInFolder = itemsInFolder,
                showAddDialog = showAddDialog,
                showDeleteDialog = showDeleteDialog,
                showMoveDialog = showMoveDialog
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

    override fun toggleShowAddDialog() {
        _showAddDialog.value = !_showAddDialog.value
    }
    override fun toggleDeleteDialog() {
        _showDeleteDialog.value = !_showDeleteDialog.value
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

    override fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        viewModelScope.launch {
            folderRepository.upsertFolder(newFolder)
        }
    }

    override fun deleteFolders(deleteFolderIdList: List<Int>) {
        if (deleteFolderIdList.contains(_selectedFolderId.value))
            _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id

        viewModelScope.launch {
            folderRepository.deleteFoldersById(deleteFolderIdList)
            savedItemRepository.deleteSavedItemsByFolderId(deleteFolderIdList)
        }
    }

    override fun moveFolder(destFolderId: Int) {
        if (_targetItem.value == null) return

        val targetItem = _targetItem.value!!
        if (destFolderId == targetItem.folderId) return
        viewModelScope.launch {
            savedItemRepository.moveSavedItem(targetItem.imageUrl, destFolderId)
        }
    }

}
