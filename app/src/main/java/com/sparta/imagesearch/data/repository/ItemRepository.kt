package com.sparta.imagesearch.data.repository

interface ItemRepository {
    suspend fun getItems(query: String): List<Item>
}