package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.source.local.folder.FolderId

@Composable
fun DeleteFolderDialog(
    modifier: Modifier = Modifier,
    folders: List<FolderModel>,
    onDismissRequest: () -> Unit,
    deleteFolder: (List<String>) -> Unit
) {
    val selectedFoldersId = remember { mutableStateListOf<String>() }

    val enableDeleteButton = remember { derivedStateOf { !selectedFoldersId.isEmpty() } }

    var showAlertDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = showAlertDialog) {
        DeleteAlertDialog(
            onAlertDismiss = {
                showAlertDialog = false
            },
            onAlertConfirm = {
                deleteFolder(selectedFoldersId.toList())
                onDismissRequest()
            }
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier.heightIn(max = 300.dp),
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
                    folders = folders,
                    selected = { folderId -> selectedFoldersId.contains(folderId) },
                    onSelect = { folderId ->
                        with(selectedFoldersId) {
                            if (!contains(folderId)) add(folderId)
                            else remove(folderId)
                        }
                    }
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    DeleteDialogCloseButton(
                        modifier = modifier,
                        buttonLabel = stringResource(R.string.delete_folder_negative),
                        onClick = onDismissRequest
                    )
                    Spacer(modifier = modifier.size(12.dp))
                    DeleteDialogConfirmButton(
                        modifier = modifier,
                        buttonLabel = stringResource(R.string.delete_folder_positive),
                        onClick = { showAlertDialog = true },
                        enabled = enableDeleteButton.value
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteFolderList(
    modifier: Modifier = Modifier,
    folders: List<FolderModel>,
    selected: (String) -> Boolean,
    onSelect: (String) -> Unit
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
    folder: FolderModel,
    selected: Boolean,
    onSelect: (String) -> Unit
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
                    contentDescription = stringResource(id = R.string.delete_folder_icon_select_check_desc)
                )
            } else {
                Image(
                    modifier = modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.icon_select_empty),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = stringResource(id = R.string.delete_folder_icon_select_empty_desc)
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
fun DeleteDialogCloseButton(
    modifier: Modifier = Modifier,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = buttonLabel)
    }
}

@Composable
fun DeleteDialogConfirmButton(
    modifier: Modifier = Modifier,
    buttonLabel: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = buttonLabel)
    }
}

@Composable
fun DeleteAlertDialog(
    modifier: Modifier = Modifier,
    onAlertDismiss: () -> Unit,
    onAlertConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onAlertDismiss
    ) {
        Card(
            modifier = modifier.height(IntrinsicSize.Min),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = modifier
                        .scale(1.2f)
                        .padding(bottom = 16.dp),
                    painter = painterResource(id = R.drawable.icon_warning),
                    contentDescription = ""
                )
                Text(
                    text = stringResource(R.string.warning_delete_title),
                )
                Text(
                    text = stringResource(R.string.warning_delete_body),
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAlertDismiss
                    ) {
                        Text(text = stringResource(R.string.warning_negative))
                    }
                    Spacer(modifier = modifier.size(12.dp))
                    Button(
                        onClick = onAlertConfirm
                    ) {
                        Text(text = stringResource(R.string.warning_positive))
                    }
                }
            }
        }
    }
}


