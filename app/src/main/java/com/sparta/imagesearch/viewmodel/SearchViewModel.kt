package com.sparta.imagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparta.imagesearch.data.Folder
import com.sparta.imagesearch.data.FolderId
import com.sparta.imagesearch.data.Item
import com.sparta.imagesearch.data.ItemType
import com.sparta.imagesearch.pref_util.KeywordPrefManager
import com.sparta.imagesearch.pref_util.SavedItemPrefManager
import com.sparta.imagesearch.retrofit.ImageDocument
import com.sparta.imagesearch.retrofit.ImageResponse
import com.sparta.imagesearch.retrofit.SearchClient
import com.sparta.imagesearch.retrofit.VideoDocument
import com.sparta.imagesearch.retrofit.VideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchViewModel : ViewModel() {
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
        _savedItems.value = SavedItemPrefManager.loadSavedItems()
        Log.d(TAG, "loadSavedItems) size: ${savedItems.value!!.size}")
    }

    private fun saveSavedItems() {
        SavedItemPrefManager.saveSavedItems(savedItems.value!!)
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
        if (query.isEmpty()) {
            withContext(Dispatchers.Main) {
                _searchItems.value = emptyList()
            }
            return
        }

        var imageResponse: ImageResponse? = null
        var videoResponse: VideoResponse? = null

        val imageSearchJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                Log.d(TAG, "getting image response...")
                imageResponse = SearchClient.searchNetWork.getImageResponse(query)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
        }
        val videoSearchJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                Log.d(TAG, "getting video response...")
                videoResponse = SearchClient.searchNetWork.getVideoResponse(query)
            } catch (e: Exception) {
                e.printStackTrace()
                cancel()
            }
        }

        imageSearchJob.join()
        videoSearchJob.join()

        val newDataset = getNewDataset(imageResponse, videoResponse)
        withContext(Dispatchers.Main) {
            _searchItems.value = newDataset
        }
    }

    private fun getNewDataset(
        imageResponse: ImageResponse?,
        videoResponse: VideoResponse?
    ): MutableList<Item> {
        val newDataset = mutableListOf<Item>()
        newDataset += imageResponse?.convertDataset() ?: mutableListOf()
        newDataset += videoResponse?.convertDataset() ?: mutableListOf()

        newDataset.run {
            sortWith { item1, item2 ->
                item1.time.compareTo(item2.time)
            }
            reverse()
        }
        return newDataset.toMutableList()
    }

    private fun ImageResponse.convertDataset(): MutableList<Item> {
        val newDataset = mutableListOf<Item>()
        documents?.forEach {
            newDataset.add(it.convert())
        }
        return newDataset
    }

    private fun VideoResponse.convertDataset(): MutableList<Item> {
        val newDataset = mutableListOf<Item>()
        documents?.forEach {
            newDataset.add(it.convert())
        }
        return newDataset
    }

    private fun ImageDocument.convert() =
        Item(
            itemType = ItemType.IMAGE_TYPE,
            imageUrl = image_url,
            source = display_sitename,
            time = LocalDateTime
                .parse(datetime, DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )

    private fun VideoDocument.convert() =
        Item(
            itemType = ItemType.VIDEO_TYPE,
            imageUrl = thumbnail,
            source = author,
            time = LocalDateTime
                .parse(datetime, DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
}