package com.sparta.imagesearch.presentation.search

import com.sparta.imagesearch.domain.Item

interface SearchScreenInputs {
    fun updateKeyword(newKeyword: String)
    fun saveItem(item: Item)
    fun getFolderColorHexById(folderId:Int): String
}