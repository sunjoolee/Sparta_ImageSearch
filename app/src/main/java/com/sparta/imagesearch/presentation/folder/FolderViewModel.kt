package com.sparta.imagesearch.presentation.folder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparta.imagesearch.entity.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.data.source.local.folder.Folder
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.data.source.local.folder.FolderPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository
) : ViewModel() {
    private val TAG = "FolderViewModel"

    private val _selectedFolderId = MutableLiveData<String>(FolderId.DEFAULT_FOLDER.id)
    val selectedFolderId: LiveData<String> get() = _selectedFolderId

    private val _folderModels = MutableLiveData<List<FolderModel>>(emptyList())
    val folderModels: LiveData<List<FolderModel>> get() = _folderModels

    private val _resultFolderModels = MediatorLiveData<List<FolderModel>>().apply {
        addSource(folderModels) { folderModels ->
            Log.d(TAG, "_resultFolderModels.onChange) folderModels changed")
            value = folderModels.map { it.copy(isSelected = (it.id == selectedFolderId.value!!)) }
        }
        addSource(selectedFolderId) { selectedFolderId ->
            Log.d(TAG, "_resultFolderModels.onChange) selectedFolderId changed")
            loadItemsInFolder()
            value = folderModels.value!!.map { it.copy(isSelected = (it.id == selectedFolderId)) }
        }
    }
    val resultFolderModels: LiveData<List<FolderModel>> get() = _resultFolderModels

    private val _itemsInFolder = MutableLiveData<List<Item>>(emptyList())
    val itemsInFolder: LiveData<List<Item>> get() = _itemsInFolder

    private fun loadFolders() {
        _folderModels.value = FolderPrefManager.loadFolders().map { it.convert() }
        if (folderModels.value!!.isEmpty())
            _folderModels.value =
                listOf(Folder.getDefaultFolder().convert().copy(isSelected = true))
        Log.d(TAG, "loadFolders) folderModels.size: ${folderModels.value!!.size}")
    }

    private fun saveFolders() {
        Log.d(TAG, "saveFolders) folderModels.size: ${folderModels.value!!.size}")
        FolderPrefManager.saveFolders(folderModels.value!!.map { it.convert() })
    }

    fun selectFolder(folderModel: FolderModel) {
        _selectedFolderId.value = folderModel.id
    }

    fun deleteFolders(deleteFolderIdList: List<String>) {
        if (deleteFolderIdList.contains(selectedFolderId.value))
            _selectedFolderId.value = FolderId.DEFAULT_FOLDER.id

        _folderModels.value = folderModels.value!!.filterNot {
            deleteFolderIdList.contains(it.id)
        }

        savedItemRepository.deleteFolderSavedItems(deleteFolderIdList)
    }

    fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        _folderModels.value = _folderModels.value!! + listOf(newFolder.convert())
    }

    fun moveFolder(item: Item, destFolderId: String) {
        if (destFolderId == item.folderId) return
        savedItemRepository.moveSavedItem(item.id, destFolderId)

        _itemsInFolder.value = _itemsInFolder.value!!.filterNot { it.id == item.id }
    }

    private fun loadItemsInFolder() {
        Log.d(TAG, "loadItemsInFolder) called")
        _itemsInFolder.value =
            savedItemRepository.loadFolderSavedItems(selectedFolderId.value!!)

    }

    fun unSaveItem(item: Item) {
        savedItemRepository.deleteSavedItem(item)
        _itemsInFolder.value = _itemsInFolder.value!!.filterNot { it.id == item.id }
    }

    fun loadState() {
        loadFolders()
        loadItemsInFolder()
    }

    fun saveState() {
        saveFolders()
    }

    private fun Folder.convert() =
        FolderModel(id, name, colorHex, id == selectedFolderId.value)

    private fun FolderModel.convert() =
        Folder(id, name, colorHex)
}