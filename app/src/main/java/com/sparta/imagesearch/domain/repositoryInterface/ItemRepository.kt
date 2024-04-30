package com.sparta.imagesearch.domain.repositoryInterface

import com.sparta.imagesearch.entity.Item

interface ItemRepository {
    suspend fun getItems(query: String): List<Item>
}