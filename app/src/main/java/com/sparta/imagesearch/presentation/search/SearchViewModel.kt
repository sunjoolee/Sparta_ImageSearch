package com.sparta.imagesearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.domain.repositoryInterface.KeywordRepository
import com.sparta.imagesearch.domain.repositoryInterface.SavedItemRepository
import com.sparta.imagesearch.domain.usecase.GetFoldersUsecase
import com.sparta.imagesearch.domain.usecase.GetSearchItemsUsecase
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

interface SearchScreenInputs {
    fun updateKeyword(newKeyword: String)
    fun saveItem(item: Item)
    fun getFolderColorHexById(folderId:Int): String
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedItemRepository: SavedItemRepository,
    private val keywordRepository: KeywordRepository,
    private val getFoldersUsecase: GetFoldersUsecase,
    private val getSearchItemsUsecase: GetSearchItemsUsecase
) : ViewModel(), SearchScreenInputs {
    private val TAG = this::class.java.simpleName

    private val _keyword = MutableStateFlow("")
    private val _searchItems = MutableStateFlow<List<Item>>(emptyList())
    private val _savedItems = MutableStateFlow<List<Item>>(emptyList())
    private val _resultItems = MutableStateFlow<List<Item>>(emptyList())
    private val _folders = MutableStateFlow<List<Folder>>(emptyList())

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    val inputs = this@SearchViewModel

    init {
        viewModelScope.launch {
            getFoldersUsecase.invoke().collect {
                _folders.value = it
            }
        }
        viewModelScope.launch {
            keywordRepository.getKeyword().collect {
                _keyword.value = it
            }
        }
        viewModelScope.launch {
            _keyword.collect {
                Log.d(TAG, "collect keyword) keyword: $it")
                getSearchItems()
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

    override fun updateKeyword(newKeyword: String) {
        Log.d(TAG, "updateKeyword) newKeyword: $newKeyword")
        viewModelScope.launch {
            keywordRepository.setKeyword(newKeyword)
        }
    }

    override fun saveItem(item: Item) {
        if (_savedItems.value.find { it.imageUrl == item.imageUrl } == null) {
            viewModelScope.launch {
                savedItemRepository.saveSavedItem(item.copy(folderId = FolderId.DEFAULT_FOLDER.id))
            }
        } else {
            viewModelScope.launch {
                savedItemRepository.deleteSavedItem(item)
            }
        }
    }

    override fun getFolderColorHexById(folderId: Int): String =
        _folders.value.find { it.id == folderId }?.colorHex ?: FolderColor.NO_COLOR.colorHex

    private suspend fun getSearchItems() {
        getSearchItemsUsecase(_keyword.value).collect {
            _searchItems.value = it
        }
    }
}