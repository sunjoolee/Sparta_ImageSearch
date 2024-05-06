package com.sparta.imagesearch.data.source.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sparta.imagesearch.data.ApiResponse
import com.sparta.imagesearch.data.mappers.toItemEntity
import com.sparta.imagesearch.data.repository.KakaoSearchRepositoryImpl
import com.sparta.imagesearch.data.source.local.item.ItemDatabase
import com.sparta.imagesearch.data.source.local.item.ItemEntity
import com.sparta.imagesearch.data.source.local.keyword.KeywordSharedPref
import kotlinx.coroutines.flow.combine
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ItemRemoteMediator @Inject constructor(
    private val itemDatabase: ItemDatabase,
    private val kakaoSearchRepositoryImpl: KakaoSearchRepositoryImpl

) : RemoteMediator<Int, ItemEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ItemEntity>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        Log.d("ItemRemoteMediator", "LoadType.APPEND) lastItem.id: ${lastItem.id}")
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val keyword = KeywordSharedPref.loadKeyword()
            Log.d("ItemRemoteMediator", "load) loadKey: $loadKey, keyword: $keyword")

            val imageResponseFlow =
                kakaoSearchRepositoryImpl.getImages(
                    query = keyword,
                    page = loadKey,
                    pageSize = state.config.pageSize
                )
            val videoResponseFlow =
                kakaoSearchRepositoryImpl.getVideos(
                    query = keyword,
                    page = loadKey,
                    pageSize = state.config.pageSize
                )

            val itemEntityList = mutableListOf<ItemEntity>()
            imageResponseFlow.combine(videoResponseFlow) { i, v ->
                if (i is ApiResponse.Success) {
                    itemEntityList.addAll(
                        i.data.documents?.map { it.toItemEntity(keyword) } ?: emptyList()
                    )
                }
                if (v is ApiResponse.Success) {
                    itemEntityList.addAll(
                        v.data.documents?.map { it.toItemEntity(keyword) } ?: emptyList()
                    )
                }
                itemEntityList.toList().sortedBy { it.time }
            }.collect {
                itemDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        itemDatabase.getItemDAO().deleteAllItems()
                    }
                    itemDatabase.getItemDAO().upsertAllItems(itemEntityList)
                }
            }
            return MediatorResult.Success(
                //endOfPaginationReached = itemEntityList.isEmpty()
                endOfPaginationReached = false
            )
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}