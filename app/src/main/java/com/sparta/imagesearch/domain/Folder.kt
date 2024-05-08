package com.sparta.imagesearch.domain

data class Folder(
    val id: Int = FolderId.NO_FOLDER.id,
    val name: String,
    val colorHex: String
)

data object DefaultFolder {
    val id: Int = -1
    val name: String = "기본폴더"
    val colorHex: String = FolderColor.COLOR1.colorHex
}

enum class FolderColor(val colorHex: String) {
    NO_COLOR("#858585"),
    COLOR1("#FCC233"),
    COLOR2("#F95B19"),
    COLOR3("#CCF919"),
    COLOR4("#F919E2"),
    COLOR5("#7CF6F6")
}

enum class FolderId(val id: Int) {
    NO_FOLDER(0),
    DEFAULT_FOLDER(1)
}
