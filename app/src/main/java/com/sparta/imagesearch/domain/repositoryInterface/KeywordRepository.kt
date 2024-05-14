package com.sparta.imagesearch.domain.repositoryInterface

import kotlinx.coroutines.flow.Flow

interface KeywordRepository {
    suspend fun getKeyword(): Flow<String>
    suspend fun setKeyword(keyword: String)
}