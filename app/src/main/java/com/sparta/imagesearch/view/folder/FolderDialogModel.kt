package com.sparta.imagesearch.view.folder

interface OnFolderDialogModelClickListener {
    fun onFolderDialogModelClick(folderDialogModel: FolderDialogModel)
}

data class FolderDialogModel(
    val id: String,
    val name: String,
    val colorHex: String,
    val isChecked: Boolean = false
) {
    companion object {
        const val NO_FOLDER_ID = "-1"
        const val DEFAULT_FOLDER_ID = "0"
    }
}
