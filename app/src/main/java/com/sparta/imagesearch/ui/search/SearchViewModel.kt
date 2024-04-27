package com.sparta.imagesearch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.repository.ItemRepository
import com.sparta.imagesearch.data.repository.SavedItemRepository
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.data.source.local.keyword.KeywordPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val savedItemRepository: SavedItemRepository,
) : ViewModel() {
    private val TAG = "SearchModel"

    private val _keyword = MutableLiveData<String>("")
    val keyword: LiveData<String> get() = _keyword

    private val _searchItems = MutableLiveData<List<Item>>(emptyList())
    val searchItems: LiveData<List<Item>> get() = _searchItems

    private val _savedItems = MutableLiveData<List<Item>>(emptyList())
    val savedItems: LiveData<List<Item>> get() = _savedItems

    private val _resultItems = MediatorLiveData<List<Item>>().apply {
        val onChange = { _: List<Item> ->
            value = searchItems.value!!.map { item ->
                savedItems.value!!.find { it.id == item.id }?.let {
                    item.copy(folderId = it.folderId)
                } ?: item.copy(folderId = FolderId.NO_FOLDER.id)
            }
        }
        addSource(searchItems, onChange)
        addSource(savedItems, onChange)
    }
    val resultItems: LiveData<List<Item>> get() = _resultItems

    private fun loadKeyword() {
        _keyword.value = KeywordPrefManager.loadKeyword()
        Log.d(TAG, "loadKeyword) keyword: ${_keyword.value}")
    }

    private fun saveKeyword() {
        KeywordPrefManager.saveKeyword(keyword.value!!)
        Log.d(TAG, "saveKeyword) keyword: ${_keyword.value}")
    }

    private fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    private fun loadSavedItems() {
        _savedItems.value = savedItemRepository.loadSavedItems()
        Log.d(TAG, "loadSavedItems) size: ${savedItems.value!!.size}")

    }

    private fun saveSavedItems() {
        savedItemRepository.saveSavedItems(savedItems.value!!)
        Log.d(TAG, "saveSavedItems) size: ${savedItems.value!!.size}")
    }

    fun saveItem(item: Item) {
        _savedItems.value = with(savedItems.value!!) {
            if (this.find { it.id == item.id } != null) {
                this.filterNot { it.id == item.id }
            } else {
                this + listOf(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            }
        }
    }

    fun saveState() {
        saveKeyword()
        saveSavedItems()
    }

    fun loadState() {
        loadKeyword()
        loadSavedItems()
    }

    fun search(keyword: String) {
        setKeyword(keyword)
        CoroutineScope(Dispatchers.Default).launch {
            fetchSearchResult()
        }
    }

    private suspend fun fetchSearchResult() {
        val query = _keyword.value ?: ""
        val newDataset = itemRepository.getItems(query)
        withContext(Dispatchers.Main) {
            _searchItems.value = newDataset
        }
    }
}