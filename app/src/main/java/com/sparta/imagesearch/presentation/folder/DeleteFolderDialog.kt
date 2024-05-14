package com.sparta.imagesearch.presentation.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.util.dialog.BaseDialog
import com.sparta.imagesearch.presentation.util.dialog.DialogButton
import com.sparta.imagesearch.presentation.util.dialog.DialogContent
import com.sparta.imagesearch.presentation.util.dialog.DialogTitle

@Composable
fun DeleteFolderDialog(
    viewModel: DeleteFolderDialogViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
    val deleteFolderDialogState by viewModel.state.collectAsState()
    val deleteFolderDialogInputs = viewModel.inputs

    BaseDialog(
        dialogTitle = DialogTitle.DefaultDialogTitle(R.string.delete_folder_title),
        dialogContent = DialogContent.FolderListDialogContent(
            folders = deleteFolderDialogState.folders,
            selectedFolderIds = deleteFolderDialogState.deleteFoldersId,
            selectedIconId = R.drawable.icon_select_check,
            onFolderSelectById = deleteFolderDialogInputs::selectDeleteFolderId
        ),
        dialogNegButton = DialogButton.NegativeDialogButton(
            labelId = R.string.delete_folder_negative,
            onClick = {
                deleteFolderDialogInputs.clearDeleteFoldersId()
                onDismissRequest()
            }
        ),
        dialogPosButton = DialogButton.PositiveDialogButton(
            labelId = R.string.delete_folder_positive,
            onClick = {
                deleteFolderDialogInputs.deleteFolders()
                deleteFolderDialogInputs.clearDeleteFoldersId()
                onDismissRequest()
            },
            enabled = deleteFolderDialogState.canDeleteFolders
        ),
        onDismissRequest = onDismissRequest
    )
}



