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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenState(
    val keyword: String = "",
    val resultItems: List<Item> = emptyList(),
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository,
    private val savedItemRepository: SavedItemRepository,
) : ViewModel() {
    private val TAG = "SearchModel"

    private val _keyword = MutableStateFlow("")
    private val _searchItems = MutableStateFlow<List<Item>>(emptyList())
    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val _resultItems = MutableStateFlow<List<Item>>(emptyList())

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState())
    val state = _state.asStateFlow()

    init {
        initKeyword()

        viewModelScope.launch {
            _keyword.collect {
                Log.d(TAG, "_keyword.collect) called")
                loadSearchItems()
            }
        }
        viewModelScope.launch {
            _searchItems.combine(_savedItems) { searchItems, savedItems ->
                Log.d(TAG, "searchItems.combine(savedItems) called")
                searchItems.map { searchItem ->
                    val savedItem = savedItems.find { it.imageUrl == searchItem.imageUrl }

                    savedItem?.let {
                        Log.d(TAG, "savedItem found, item url: ${it.imageUrl}")
                        searchItem.copy(folderId = it.folderId)
                    } ?: searchItem
                }
            }.collect {
                _resultItems.value = it
            }
        }
        viewModelScope.launch {
            _keyword.combine(_resultItems) { keyword, resultItems ->
                SearchScreenState(keyword, resultItems)
            }.collect {
                _state.value = it
            }
        }
        viewModelScope.launch {
            savedItemRepository.getAllSavedItems().collect {
                _savedItems.value = it
                Log.d(TAG, "savedItemRepository.getAllSavedItems().collect) size: ${_savedItems.value.size}")
            }
        }
    }

    private fun initKeyword() {
        _keyword.value = KeywordSharedPref.loadKeyword()
        Log.d(TAG, "initKeyword) keyword: ${_keyword.value}")
    }

    fun setKeyword(newKeyword: String) {
        _keyword.value = newKeyword
        KeywordSharedPref.saveKeyword(newKeyword)
        Log.d(TAG, "setKeyword) keyword: ${_keyword.value}")
    }

    fun saveItem(item: Item) {
        Log.d(TAG, "saveItem) called, item url: ${item.imageUrl}")
        Log.d(TAG, "saveItem) saved items size: ${_savedItems.value.size}")

        if (item.folderId == FolderId.NO_FOLDER.id) {
            Log.d(TAG, "saveItem) add to saved items")
            _savedItems.value += listOf(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            viewModelScope.launch {
                savedItemRepository.saveSavedItem(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            }
        } else {
            Log.d(TAG, "saveItem) delete from saved items")
            _savedItems.value = _savedItems.value.filterNot { it.imageUrl == item.imageUrl }
            viewModelScope.launch {
                savedItemRepository.deleteSavedItem(item)
            }
        }
    }

    private suspend fun loadSearchItems() {
        val query = _keyword.value
        val imageResponseFlow = kakaoSearchRepository.getImages(query)
        val videoResponseFlow = kakaoSearchRepository.getVideos(query)
        imageResponseFlow.combine(videoResponseFlow) { i, v ->
            val itemList = mutableListOf<Item>()
            if (i is ApiResponse.Success) {
                itemList.addAll(i.data.documents?.map { it.toItem() } ?: emptyList())
            }
            if (v is ApiResponse.Success) {
                itemList.addAll(v.data.documents?.map { it.toItem() } ?: emptyList())
            }
            itemList.toList().sortedBy { it.time }
        }.collect {
            _searchItems.value = it
        }
    }
}