package com.sparta.imagesearch.presentation.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.util.dialog.BaseDialog
import com.sparta.imagesearch.presentation.util.dialog.DialogButton
import com.sparta.imagesearch.presentation.util.dialog.DialogContent
import com.sparta.imagesearch.presentation.util.dialog.DialogTitle

@Composable
fun MoveFolderDialog(
    modifier: Modifier = Modifier,
    viewModel: MoveFolderDialogViewModel = hiltViewModel(),
    targetItem: Item?,
    onDismissRequest: () -> Unit
) {
    val moveFolderDialogState by viewModel.state.collectAsState()
    val moveFolderDialogInputs = viewModel.inputs

    if (targetItem == null) onDismissRequest()
    else moveFolderDialogInputs.setTargetItem(targetItem)

    BaseDialog(
        dialogTitle = DialogTitle.DefaultDialogTitle(R.string.move_folder_title),
        dialogContent = DialogContent.FolderListDialogContent(
            folders = moveFolderDialogState.folders,
            selectedFolderIds = listOf(moveFolderDialogState.targetFolderId),
            selectedIconId = R.drawable.icon_select_full,
            onFolderSelectById = moveFolderDialogInputs::selectTargetFolderId
        ),
        dialogNegButton = DialogButton.NegativeDialogButton(
            labelId = R.string.move_folder_negative,
            onClick = onDismissRequest
        ),
        dialogPosButton = DialogButton.PositiveDialogButton(
            labelId = R.string.move_folder_positive,
            onClick = {
                moveFolderDialogInputs.moveFolder()
                onDismissRequest()
            }
        ),
        onDismissRequest = onDismissRequest
    )
}




