package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.Item

interface ItemRepository {
    suspend fun getItems(query: String): List<Item>
}