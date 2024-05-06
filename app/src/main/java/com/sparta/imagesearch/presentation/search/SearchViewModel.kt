package com.sparta.imagesearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.data.source.local.item.ItemEntity
import com.sparta.imagesearch.data.source.local.keyword.KeywordSharedPref
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository,
    itemPager: Pager<Int, ItemEntity>
) : ViewModel() {
    private val TAG = "SearchModel"

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> get() = _keyword

    private val _searchItems = MutableStateFlow<List<Item>>(emptyList())
    private val searchItems = _searchItems.asStateFlow()

    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val savedItems = _savedItems.asStateFlow()

    private val _resultItems =
        searchItems.combine(savedItems) { searchItems, savedItems ->
            searchItems.map { item ->
                savedItems.find { it.imageUrl == item.imageUrl }?.let {
                    item.copy(folderId = it.folderId)
                } ?: item.copy(folderId = FolderId.NO_FOLDER.id)
            }
        }
    val resultItems: Flow<List<Item>> get() = _resultItems

    var itemPagingFlow = itemPager
        .flow
        .map { pagingData ->
            pagingData
                .filter { itemEntity -> itemEntity.searchKeyword == _keyword.value }
                .map { itemEntity -> itemEntity.toItem() }
        }
        .cachedIn(viewModelScope)

    init {
        loadState()
    }

    private fun loadKeyword() {
        _keyword.value = KeywordSharedPref.loadKeyword()
        Log.d(TAG, "loadKeyword) keyword: ${_keyword.value}")
    }

    private fun saveKeyword() {
        KeywordSharedPref.saveKeyword(keyword.value)
        Log.d(TAG, "saveKeyword) keyword: ${_keyword.value}")
    }

    private fun setKeyword(keyword: String) {
        _keyword.value = keyword
        saveKeyword()
    }

    private fun loadSavedItems() {
        viewModelScope.launch {
            savedItemRepository.loadSavedItems().collect {
                _savedItems.value = it
            }
            Log.d(TAG, "loadSavedItems) size: ${savedItems.value.size}")
        }

    }

    private fun saveSavedItems() {
        savedItemRepository.saveSavedItems(savedItems.value)
        Log.d(TAG, "saveSavedItems) size: ${savedItems.value.size}")
    }

    fun saveItem(item: Item) {
        _savedItems.value = with(savedItems.value) {
            if (this.find { it.imageUrl == item.imageUrl } != null) {
                this.filterNot { it.imageUrl == item.imageUrl }
            } else {
                this + listOf(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            }
        }
    }

    private fun saveState() {
        Log.d(TAG, "saveState) called")
        saveKeyword()
        saveSavedItems()
    }

    private fun loadState() {
        Log.d(TAG, "loadState) called")
        loadKeyword()
        loadSavedItems()
    }

    fun search(keyword: String) {
        setKeyword(keyword)
        viewModelScope.launch {
            fetchSearchResult()
        }
    }

    private suspend fun fetchSearchResult() {
        // TODO fetch items where search keyword is keyword?
    }

    override fun onCleared() {
        saveState()
        super.onCleared()
    }
}