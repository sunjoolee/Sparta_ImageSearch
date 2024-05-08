package com.sparta.imagesearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.mappers.toItem
import com.sparta.imagesearch.data.source.local.keyword.KeywordSharedPref
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.KakaoSearchRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository,
    private val savedItemRepository: SavedItemRepository,
) : ViewModel() {
    private val TAG = "SearchModel"

    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

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

    init{
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
    }

    private fun loadSavedItems() {
        viewModelScope.launch {
            savedItemRepository.loadSavedItems().collect{
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
        saveKeyword()
        saveSavedItems()
    }

    private fun loadState() {
        loadKeyword()
        loadSavedItems()
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            fetchSearchResult()
        }
    }

    private suspend fun fetchSearchResult() {
        val query = _keyword.value ?: ""

        val imageResponseFlow = kakaoSearchRepository.getImages(query)
        val videoResponseFlow = kakaoSearchRepository.getVideos(query)

        imageResponseFlow.combine(videoResponseFlow) {i, v ->
            val itemList = mutableListOf<Item>()
            if(i is ApiResponse.Success){
                itemList.addAll(i.data.documents?.map { it.toItem() } ?: emptyList())
            }
            if(v is ApiResponse.Success){
                itemList.addAll(v.data.documents?.map { it.toItem() } ?: emptyList())
            }
            itemList.toList()
        }.collect{
            _searchItems.value = it
        }

    }
    override fun onCleared() {
        saveState()
        super.onCleared()
    }
}