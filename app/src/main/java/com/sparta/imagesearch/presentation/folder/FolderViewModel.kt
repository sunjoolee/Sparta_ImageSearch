package com.sparta.imagesearch.presentation.folder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.domain.DefaultFolder
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.FolderRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {
    private val TAG = "FolderViewModel"

    private val _selectedFolderId = MutableStateFlow(DefaultFolder.id)
    val selectedFolderId = _selectedFolderId.asStateFlow()

    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    val folders = _folders.asStateFlow()

    private val _itemsInFolder = MutableStateFlow<List<Item>>(emptyList())
    val itemsInFolder = _itemsInFolder.asStateFlow()

    init {
        loadState()
    }

    private fun loadFolders() {
        Log.d(TAG, "loadFolders) called")
        viewModelScope.launch {
            folderRepository.getAllFolders().collect {
                _folders.value = it
                Log.d(TAG, "loadFolders) folders.size: ${folders.value.size}")
            }
        }
    }

    private fun saveFolders() {
        viewModelScope.launch {
            Log.d(TAG, "saveFolders) folders.size: ${folders.value.size}")
            folderRepository.upsertFolders(folders.value)
        }
    }

    fun selectFolder(folder: Folder) {
        _selectedFolderId.value = folder.id
    }

    fun deleteFolders(deleteFolderIdList: List<Int>) {
        if (deleteFolderIdList.contains(selectedFolderId.value))
            _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id

        _folders.value = folders.value.filterNot {
            deleteFolderIdList.contains(it.id)
        }
        savedItemRepository.deleteFolderSavedItems(deleteFolderIdList)
    }

    fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        _folders.value += listOf(newFolder)
    }

    fun moveFolder(item: Item, destFolderId: Int) {
        if (destFolderId == item.folderId) return
        savedItemRepository.moveSavedItem(item.imageUrl, destFolderId)

        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
    }

    private fun loadItemsInFolder() {
        Log.d(TAG, "loadItemsInFolder) called")
        viewModelScope.launch {
            savedItemRepository.loadFolderSavedItems(selectedFolderId.value).collect {
                _itemsInFolder.value = it
            }
        }
    }

    fun unSaveItem(item: Item) {
        savedItemRepository.deleteSavedItem(item)
        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
    }

    private fun loadState() {
        loadFolders()
        loadItemsInFolder()
    }

    private fun saveState() {
        saveFolders()
    }

    override fun onCleared() {
        saveState()
        super.onCleared()
    }
}