package com.sparta.imagesearch.presentation.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.presentation.util.dialog.BaseDialog
import com.sparta.imagesearch.presentation.util.dialog.DialogButton
import com.sparta.imagesearch.presentation.util.dialog.DialogContent
import com.sparta.imagesearch.presentation.util.dialog.DialogTitle


@Composable
fun AddFolderDialog(
    modifier: Modifier = Modifier,
    viewModel: AddFolderDialogViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
    val addFolderDialogState by viewModel.state.collectAsState()
    val addFolderDialogInputs = viewModel.inputs

    BaseDialog(
        dialogTitle = DialogTitle.DefaultDialogTitle(R.string.add_folder_title),
        dialogContent = DialogContent.FolderInputDialogContent(
            folderName = addFolderDialogState.folderName,
            onFolderNameChange = addFolderDialogInputs::setFolderName,
            colorHexes = FolderColor.entries.map { it.colorHex }.drop(1),
            selectedColorHex = addFolderDialogState.folderColorHex,
            onColorHexSelect = addFolderDialogInputs::setFolderColorHex
        ),
        dialogNegButton = DialogButton.NegativeDialogButton(
            labelId = R.string.add_folder_negative,
            onClick = onDismissRequest
        ),
        dialogPosButton = DialogButton.PositiveDialogButton(
            labelId = R.string.add_folder_positive,
            onClick = {
                addFolderDialogInputs.addFolder()
                onDismissRequest()
            }
        ),
        onDismissRequest = onDismissRequest
    )
}