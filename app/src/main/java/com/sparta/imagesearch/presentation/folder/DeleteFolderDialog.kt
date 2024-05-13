package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.presentation.util.dialog.BaseDialog
import com.sparta.imagesearch.presentation.util.dialog.DialogButton
import com.sparta.imagesearch.presentation.util.dialog.DialogButtonWrapper
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
                deleteFolderDialogInputs.clearDeleteFoldersId()
                deleteFolderDialogInputs.deleteFolders()
                onDismissRequest()
            }
        ),
        onDismissRequest = onDismissRequest
    )
}



