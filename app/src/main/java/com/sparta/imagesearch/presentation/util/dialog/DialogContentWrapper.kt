package com.sparta.imagesearch.presentation.util.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.presentation.util.dialog.dalog_content.AlertDialogContent
import com.sparta.imagesearch.presentation.util.dialog.dalog_content.FolderInputDialogContent
import com.sparta.imagesearch.presentation.util.dialog.dalog_content.FolderListDialogContent

val FOLDER_ITEM_HEIGHT_DP = 36.dp
val FOLDER_SELECT_ICON_SCALE = 1.5f


sealed interface DialogContent {
    data class AlertDialogContent(
        @StringRes val textId: Int
    ) : DialogContent

    data class FolderInputDialogContent(
        val folderName: String,
        val onFolderNameChange: (String) -> Unit,
        val colorHexes: List<String>,
        val selectedColorHex: String,
        val onColorHexSelect: (String) -> Unit
    ) : DialogContent

    data class FolderListDialogContent(
        val folders: List<Folder>,
        val selectedFolderIds: List<Int>,
        @DrawableRes val selectedIconId: Int,
        val onFolderSelectById: (Int) -> Unit,
    ) : DialogContent
}

@Composable
fun DialogContentWrapper(dialogContent: DialogContent) {
    when (dialogContent) {
        is DialogContent.AlertDialogContent -> AlertDialogContent(
            textId = dialogContent.textId
        )

        is DialogContent.FolderInputDialogContent -> FolderInputDialogContent(
            folderName = dialogContent.folderName,
            onFolderNameChange = dialogContent.onFolderNameChange,
            colorHexes = dialogContent.colorHexes,
            selectedColorHex = dialogContent.selectedColorHex,
            onColorHexSelect = dialogContent.onColorHexSelect
        )

        is DialogContent.FolderListDialogContent -> FolderListDialogContent(
            folders = dialogContent.folders,
            selectedFolderIds = dialogContent.selectedFolderIds,
            selectedIconId = dialogContent.selectedIconId,
            onFolderSelectById = dialogContent.onFolderSelectById
        )
    }
}


