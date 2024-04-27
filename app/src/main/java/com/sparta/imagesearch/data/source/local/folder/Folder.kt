package com.sparta.imagesearch.data.source.local.folder

import com.squareup.moshi.JsonClass
import java.util.UUID

enum class FolderId(val id:String){
    NO_FOLDER("no_folder_id"),
    DEFAULT_FOLDER("default_folder_id")
}

@JsonClass(generateAdapter = true)
data class Folder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val colorHex: String
) {
    companion object {
        fun getDefaultFolder() =
            Folder(
                id = FolderId.DEFAULT_FOLDER.id,
                name = "기본 폴더",
                colorHex = FolderColor.color1.colorHex
            )
    }
}
