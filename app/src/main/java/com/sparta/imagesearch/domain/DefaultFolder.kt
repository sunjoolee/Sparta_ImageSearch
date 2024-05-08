package com.sparta.imagesearch.domain

data object DefaultFolder {
    val id: Int = FolderId.DEFAULT_FOLDER.id
    val name: String = "기본폴더"
    val colorHex: String = FolderColor.COLOR1.colorHex
}