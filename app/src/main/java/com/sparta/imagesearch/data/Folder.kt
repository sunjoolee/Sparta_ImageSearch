package com.sparta.imagesearch.data

import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Folder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val colorHex: String
) {
    companion object {
        const val NO_FOLDER_ID = "-1"
        const val DEFAULT_FOLDER_ID = "0"

        fun getDefaultFolder() =
            Folder(
                id = DEFAULT_FOLDER_ID,
                name = "기본 폴더",
                colorHex = FolderColor.color1.colorHex
            )
    }
}
