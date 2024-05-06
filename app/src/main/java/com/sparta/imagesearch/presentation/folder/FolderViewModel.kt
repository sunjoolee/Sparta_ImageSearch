package com.sparta.imagesearch.presentation.folder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.source.local.folder.Folder
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.data.source.local.folder.FolderPrefManager
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository
) : ViewModel() {
    private val TAG = "FolderViewModel"

    private val _selectedFolderId = MutableStateFlow(FolderId.DEFAULT_FOLDER.id)
    val selectedFolderId = _selectedFolderId.asStateFlow()

    private val _folderModels = MutableStateFlow<List<FolderModel>>(emptyList())
    private val folderModels = _folderModels.asStateFlow()

    private val _resultFolderModels =
        folderModels.combine(selectedFolderId) { folderModels, selectedFolderId ->
            loadItemsInFolder()
            folderModels.map { it.copy(isSelected = (it.id == selectedFolderId)) }
        }
    val resultFolderModels: Flow<List<FolderModel>> get() = _resultFolderModels

    private val _itemsInFolder = MutableStateFlow<List<Item>>(emptyList())
    val itemsInFolder = _itemsInFolder.asStateFlow()

    init{
        loadState()
    }

    private fun loadFolders() {
        _folderModels.value = FolderPrefManager.loadFolders().map { it.convert() }
        if (folderModels.value.isEmpty())
            _folderModels.value =
                listOf(Folder.getDefaultFolder().convert().copy(isSelected = true))
        Log.d(TAG, "loadFolders) folderModels.size: ${folderModels.value.size}")
    }

    private fun saveFolders() {
        Log.d(TAG, "saveFolders) folderModels.size: ${folderModels.value.size}")
        FolderPrefManager.saveFolders(folderModels.value.map { it.convert() })
    }

    fun selectFolder(folderModel: FolderModel) {
        _selectedFolderId.value = folderModel.id
    }

    fun deleteFolders(deleteFolderIdList: List<String>) {
        if (deleteFolderIdList.contains(selectedFolderId.value))
            _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id

        _folderModels.value = folderModels.value.filterNot {
            deleteFolderIdList.contains(it.id)
        }

        savedItemRepository.deleteSavedItemsInFolders(deleteFolderIdList)
    }

    fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        _folderModels.value += listOf(newFolder.convert())
    }

    fun moveFolder(item: Item, destFolderId: String) {
        if (destFolderId == item.folderId) return
        savedItemRepository.moveSavedItem(item.imageUrl, destFolderId)

        _itemsInFolder.value = _itemsInFolder.value.filterNot { it.imageUrl == item.imageUrl }
    }

    private fun loadItemsInFolder() {
        Log.d(TAG, "loadItemsInFolder) called")
        viewModelScope.launch {
            savedItemRepository.loadSavedItemsInFolder(selectedFolderId.value).collect{
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

    private fun Folder.convert() =
        FolderModel(id, name, colorHex, id == selectedFolderId.value)

    private fun FolderModel.convert() =
        Folder(id, name, colorHex)
}