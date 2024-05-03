package com.sparta.imagesearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.data.source.local.keyword.KeywordSharedPref
import com.sparta.imagesearch.data.source.remote.Document
import com.sparta.imagesearch.domain.repositoryInterface.ItemRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.entity.Item
import com.sparta.imagesearch.entity.ItemType
import com.sparta.imagesearch.util.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val savedItemRepository: SavedItemRepository,
) : ViewModel() {
    private val TAG = "SearchModel"

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> get() = _keyword

    private val _searchItems = MutableStateFlow<List<Item>>(emptyList())
    private val searchItems: StateFlow<List<Item>> get() = _searchItems

    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val savedItems: StateFlow<List<Item>> get() = _savedItems

    private val _resultItems =
        searchItems.combine(savedItems) { searchItems, savedItems ->
            searchItems.map { item ->
                savedItems.find { it.id == item.id }?.let {
                    item.copy(folderId = it.folderId)
                } ?: item.copy(folderId = FolderId.NO_FOLDER.id)
            }
        }
    val resultItems: Flow<List<Item>> get() = _resultItems


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

        val imageResponseFlow = itemRepository.getImages(query)
        val videoResponseFlow = itemRepository.getVideos(query)

        imageResponseFlow.combine(videoResponseFlow) {i, v ->
            val itemList = mutableListOf<Item>()
            if(i is ApiResponse.Success){
                itemList.addAll(i.data.documents?.map { it.convert() } ?: emptyList())
            }
            if(v is ApiResponse.Success){
                itemList.addAll(v.data.documents?.map { it.convert() } ?: emptyList())
            }
            itemList.toList()
        }.collect{
            _searchItems.value = it
        }

    }

    private fun Document.ImageDocument.convert() =
        Item(
            itemType = ItemType.IMAGE_TYPE,
            imageUrl = imageUrl,
            source = displaySitename,
            time = datetime.formatDate()
        )

    private fun Document.VideoDocument.convert() =
        Item(
            itemType = ItemType.VIDEO_TYPE,
            imageUrl = thumbnail,
            source = author,
            time = datetime.formatDate()
        )
}