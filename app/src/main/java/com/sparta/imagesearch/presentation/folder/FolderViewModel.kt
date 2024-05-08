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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private fun loadState() {
        loadFolders()
        loadItemsInFolder()
    }

    private fun loadFolders() {
        Log.d(TAG, "loadFolders) called")
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.getAllFolders().collect {
                _folders.value = it
                Log.d(TAG, "loadFolders) folders.size: ${folders.value.size}")
            }
        }
    }

    fun loadItemsInFolder() {
        Log.d(TAG, "loadItemsInFolder) called")
        viewModelScope.launch(Dispatchers.IO) {
            savedItemRepository.getSavedItemsByFolderId(selectedFolderId.value).collect {
                _itemsInFolder.value = it
            }
        }
    }

    fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        _folders.value += listOf(newFolder)
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.upsertFolder(newFolder)
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
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.deleteFoldersById(deleteFolderIdList)
            savedItemRepository.deleteSavedItemsByFolderId(deleteFolderIdList)
        }
    }


    fun moveFolder(item: Item, destFolderId: Int) {
        if (destFolderId == item.folderId) return

        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
        savedItemRepository.moveSavedItem(item.imageUrl, destFolderId)
    }

    fun unSaveItem(item: Item) {
        savedItemRepository.deleteSavedItem(item)
        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
    }
}