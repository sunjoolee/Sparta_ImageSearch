package com.sparta.imagesearch.presentation.search

import com.sparta.imagesearch.domain.Item

interface SearchScreenInputs {
    fun setKeyword(newKeyword: String)
    fun saveItem(item: Item)
}