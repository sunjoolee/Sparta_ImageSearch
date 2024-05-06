package com.sparta.imagesearch.data.source.local.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sparta.imagesearch.domain.ItemType
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val imageUrl: String,
    val itemType: ItemType,
    val source: String,
    val time: String,
    val searchKeyword: String,
    val searchTime: String
)
