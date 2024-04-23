package com.sparta.imagesearch.data.local

import android.util.Log
import com.sparta.imagesearch.MyApplication
import com.sparta.imagesearch.data.FolderId
import com.sparta.imagesearch.data.Item
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


object SavedItemPrefManager {
    private val TAG = "SavedItemPrefManager"

    private const val SAVED_ITEM_PREF_NAME = "saved_item_shared_preferences"
    private const val SAVED_ITEM_PREF_KEY = "saved_item_pref_key"

    private val pref = MyApplication.appContext!!.getSharedPreferences(SAVED_ITEM_PREF_NAME, 0)

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val type = Types.newParameterizedType(List::class.java, String::class.java)

    fun moveSavedItem(item: Item, destFolderId: String) {
        saveSavedItems(loadSavedItems().map {
            if (it.id == item.id) it.copy(folderId = destFolderId) else it
        })
    }

    fun deleteSavedItem(item: Item) {
        saveSavedItems(loadSavedItems().filterNot { it.id == item.id })
    }

    fun filterSavedItems(folderIds: List<String>) {
        saveSavedItems(loadSavedItems().filter {
            folderIds.contains(it.folderId)
        })
    }

    fun loadSavedItems(): List<Item> {
        val savedItems = mutableListOf<Item>()
        try {
            moshi.adapter<List<String>>(type).fromJson(
                pref.getString(SAVED_ITEM_PREF_KEY, "") ?: ""
            )?.forEach { serializedItem ->
                moshi.adapter(Item::class.java).fromJson(serializedItem)?.let {
                    savedItems.add(it)
                }
            }
        } catch (e: JsonDataException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return savedItems.toList()
    }

    fun saveSavedItems(savedItems: List<Item>) {
        Log.d(TAG, "saveSavedItems) savedItems.size: ${savedItems.size}")
        val serializedSavedItems = savedItems
            .filterNot {
                it.folderId == FolderId.NO_FOLDER.id
            }
            .map {
                moshi.adapter(Item::class.java).toJson(it)
            }

        pref.edit().putString(
            SAVED_ITEM_PREF_KEY,
            moshi.adapter<List<String>>(type).toJson(serializedSavedItems)
        ).commit()
    }
}