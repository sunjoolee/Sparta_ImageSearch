package com.sparta.imagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.pref_util.FolderPrefManager
import com.sparta.imagesearch.pref_util.SavedItemPrefManager
import com.sparta.imagesearch.view.folder.FolderModel

class FolderViewModel : ViewModel() {

    private val _folderModels = MutableLiveData<List<FolderModel>>()
    val folderModels: LiveData<List<FolderModel>> get() = _folderModels

    private val _selectedFolderId = MutableLiveData<String>(FolderModel.DEFAULT_FOLDER_ID)
    val selectedFolderId: LiveData<String> get() = _selectedFolderId

    private val _folderItems = MutableLiveData<List<Item>>()
    val folderItems: LiveData<List<Item>> get() = _folderItems

    private val _resultFolderModels = MediatorLiveData<List<FolderModel>>().apply {
        addSource(folderModels) { folderModels ->
            value = folderModels.map { it.copy(isSelected = (it.id == selectedFolderId.value!!)) }
        }
        addSource(selectedFolderId) { selectedFolderId ->
            value = folderModels.value?.map { it.copy(isSelected = (it.id == selectedFolderId)) }
        }
    }
    val resultFolderModels: LiveData<List<FolderModel>> get() = _resultFolderModels

    init {
        _folderModels.value = listOf(Folder.getDefaultFolder().convert())
        _selectedFolderId.value = Folder.DEFAULT_FOLDER_ID

        loadFolders()
        loadFolderItems()
    }

    private fun Folder.convert() =
        FolderModel(id, name, colorHex, id == selectedFolderId.value)

    private fun FolderModel.convert() =
        Folder(id, name, colorHex)

    private fun loadFolders() {
        _folderModels.value = FolderPrefManager.loadFolders().map { it.convert() }
    }

    private fun loadFolderItems() {
        val savedItems = SavedItemPrefManager.loadSavedItems()
        _folderItems.value = savedItems.filter { it.folderId == selectedFolderId.value!! }
    }

    fun selectFolder(folderModel: FolderModel) {
        _selectedFolderId.value = folderModel.id
        loadFolderItems()
    }

    fun deleteFolders(folderIdList:List<String>){
        folderIdList.forEach { deleteFolder(it) }
        if(folderIdList.contains(selectedFolderId.value))
            _selectedFolderId.value = FolderModel.DEFAULT_FOLDER_ID
    }

    private fun deleteFolder(folderId: String) {
        if (folderId == FolderModel.DEFAULT_FOLDER_ID) return

        SavedItemPrefManager.deleteSavedItemsInFolder(folderId)
        FolderPrefManager.deleteFolder(folderId)

        _folderModels.value = _folderModels.value?.filterNot { it.id == folderId } ?: emptyList()
    }

    fun addFolder(name: String, colorHex: String) {
        val newFolder = Folder(name = name, colorHex = colorHex)
        _folderModels.value =
            _folderModels.value ?: emptyList<FolderModel>() + listOf(newFolder.convert())
        FolderPrefManager.saveFolders(folderModels.value!!.map { it.convert() })
    }

    fun unSaveItem(item:Item){
        SavedItemPrefManager.deleteSavedItem(item)
        _folderItems.value = _folderItems.value?.filterNot { it == item } ?: emptyList()
    }

    fun moveFolder(item:Item, destFolderId:String){
        if(destFolderId == item.folderId) return

        SavedItemPrefManager.moveSavedItem(item, destFolderId)
        _folderItems.value = _folderItems.value?.filterNot { it == item } ?: emptyList()
    }
}