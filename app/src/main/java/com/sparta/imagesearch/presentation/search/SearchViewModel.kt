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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenState(
    val keyword: String = "",
    val resultItems: List<Item> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository,
    private val savedItemRepository: SavedItemRepository,
) : ViewModel(), SearchScreenInputs {
    private val TAG = this::class.java.simpleName

    private val _keyword = MutableStateFlow("")
    private val _searchItems = MutableStateFlow<List<Item>>(emptyList())
    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val _resultItems = MutableStateFlow<List<Item>>(emptyList())

    private val _state = MutableStateFlow<SearchScreenState>(SearchScreenState())
    val state = _state.asStateFlow()

    val inputs = this@SearchViewModel

    init {
        initKeyword()

        viewModelScope.launch {
            _keyword.collect {
                Log.d(TAG, "collect keyword) keyword: $it")
                loadSearchItems()
            }
        }
        viewModelScope.launch {
            savedItemRepository.getAllSavedItems().collect {
                Log.d(TAG, "collect savedItem) size: ${it.size}")
                _savedItems.value = it
            }
        }

        combine(_searchItems, _savedItems) { searchItems, savedItems ->
            Log.d(TAG, "combine resultItems) savedItems.size: ${savedItems.size}")
            _resultItems.update {
                searchItems.map { searchItem ->
                    val savedItem = savedItems.find { it.imageUrl == searchItem.imageUrl }
                    if (savedItem != null) searchItem.copy(folderId = savedItem.folderId)
                    else searchItem
                }
            }
        }.launchIn(viewModelScope)

        combine(_keyword, _resultItems) { keyword, resultItems ->
            Log.d(TAG, "combine new state)")
            _state.update { SearchScreenState(keyword, resultItems) }
        }.launchIn(viewModelScope)
    }

    private fun initKeyword() {
        _keyword.value = KeywordSharedPref.loadKeyword()
        Log.d(TAG, "initKeyword) keyword: ${_keyword.value}")
    }

    override fun updateKeyword(newKeyword: String) {
        _keyword.value = newKeyword
        KeywordSharedPref.saveKeyword(newKeyword)
        Log.d(TAG, "updateKeyword) keyword: ${_keyword.value}")
    }

    override fun saveItem(item: Item) {
        Log.d(TAG, "saveItem) called, item url: ${item.imageUrl}")
        Log.d(TAG, "saveItem) saved items size: ${_savedItems.value.size}")

        if (_savedItems.value.find { it.imageUrl == item.imageUrl } == null) {
            Log.d(TAG, "saveItem) add to saved items")
            viewModelScope.launch {
                savedItemRepository.saveSavedItem(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            }
        } else {
            Log.d(TAG, "saveItem) delete from saved items")
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