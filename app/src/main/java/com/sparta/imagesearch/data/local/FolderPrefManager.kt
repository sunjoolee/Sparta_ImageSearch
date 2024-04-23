package com.sparta.imagesearch.data.local

import com.sparta.imagesearch.MyApplication
import com.sparta.imagesearch.data.Folder
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object FolderPrefManager {
    private const val FOLDER_PREF_NAME = "folder_shared_preferences"
    private const val FOLDER_PREF_KEY = "folder_pref_key"

    private val pref = MyApplication.appContext!!.getSharedPreferences(FOLDER_PREF_NAME, 0)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val type =
        Types.newParameterizedType(List::class.java, String::class.java)

    fun loadFolders(): List<Folder> {
        val folders = mutableListOf<Folder>()

        try {
            moshi.adapter<List<String>>(type).fromJson(
                pref.getString(FOLDER_PREF_KEY, "") ?: ""
            )?.forEach { serializedFolder ->
                moshi.adapter(Folder::class.java).fromJson(serializedFolder)?.let {
                    folders.add(it)
                }
            }
        }catch (e:JsonDataException){
            e.printStackTrace()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return folders.toList()
    }

    fun saveFolders(folders: List<Folder>) {
        val serializedFolders = folders.map {
                moshi.adapter(Folder::class.java).toJson(it)
            }

        pref.edit().putString(
            FOLDER_PREF_KEY,
            moshi.adapter<List<String>>(type).toJson(serializedFolders)
        ).commit()
    }
}