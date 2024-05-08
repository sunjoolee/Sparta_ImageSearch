package com.sparta.imagesearch.data.source.local.folder

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.UUID

@Entity(tableName = "folder_table")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val colorHex: String
)
