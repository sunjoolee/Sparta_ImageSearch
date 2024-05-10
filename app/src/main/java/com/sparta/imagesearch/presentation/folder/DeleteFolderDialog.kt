package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.presentation.util.AlertDialog
import com.sparta.imagesearch.presentation.util.DialogButtons

@Composable
fun DeleteFolderDialog(
    modifier: Modifier = Modifier,
    viewModel: DeleteFolderDialogViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
) {
    val deleteFolderDialogState by viewModel.state.collectAsState()
    val deleteFolderDialogInputs = viewModel.inputs

    Dialog(
        onDismissRequest = {
            deleteFolderDialogInputs.clearDeleteFoldersId()
            onDismissRequest()
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.delete_folder_title),
                )
                DeleteFolderList(
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
                    folders = deleteFolderDialogState.folders,
                    selected = { folderId ->
                        deleteFolderDialogState.deleteFoldersId.contains(folderId)
                    },
                    onSelect = deleteFolderDialogInputs::selectDeleteFolderId
                )
                DialogButtons(
                    dismissButtonLabelId = R.string.delete_folder_negative,
                    onDismissRequest = {
                        deleteFolderDialogInputs.clearDeleteFoldersId()
                        onDismissRequest()
                    },
                    confirmButtonLabelId = R.string.delete_folder_positive,
                    enableConfirmButton = deleteFolderDialogState.confirmButtonEnabled,
                    onConfirmRequest = {
                        viewModel.deleteFolders()
                        deleteFolderDialogInputs.clearDeleteFoldersId()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteFolderList(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    selected: (Int) -> Boolean,
    onSelect: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = folders, key = { it.id }) {
            DeleteFolderItem(
                folder = it,
                selected = selected(it.id),
                onSelect = onSelect
            )
        }
    }
}

@Composable
fun DeleteFolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    selected: Boolean,
    onSelect: (Int) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(4.dp)
            .clickable {
                if (folder.id == FolderId.DEFAULT_FOLDER.id)
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.delete_folder_default_toast),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                else onSelect(folder.id)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = modifier
                .padding(end = 16.dp)
                .scale(1.5f)
        ) {
            if (selected) {
                Image(
                    modifier = modifier
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.icon_select_check),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = ""
                )
            } else {
                Image(
                    modifier = modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.icon_select_empty),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = ""
                )
            }
        }
        Image(
            modifier = modifier.padding(end = 12.dp),
            painter = painterResource(id = R.drawable.icon_folder),
            colorFilter = ColorFilter.tint(Color(parseColor(folder.colorHex))),
            contentDescription = ""
        )
        Text(
            text = folder.name
        )
    }
}

@Composable
fun DeleteAlertDialog(
    modifier: Modifier = Modifier,
    onAlertDismiss: () -> Unit,
    onAlertConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        iconId = R.drawable.icon_warning,
        titleId = R.string.warning_delete_title,
        bodyId = R.string.warning_delete_body,
        dismissButtonLabelId = R.string.warning_negative,
        onAlertDismiss = onAlertDismiss,
        confirmButtonLabelId = R.string.warning_positive,
        onAlertConfirm = onAlertConfirm,
    )
}


