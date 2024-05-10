package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.sparta.imagesearch.domain.Item

@Composable
fun MoveFolderDialog(
    modifier: Modifier = Modifier,
    viewModel: MoveFolderDialogViewModel = hiltViewModel(),
    targetItem: Item?,
    onDismissRequest: () -> Unit
) {
    val moveFolderDialogState by viewModel.state.collectAsState()
    val moveFolderDialogInputs = viewModel.inputs

    if(targetItem == null) onDismissRequest()
    else moveFolderDialogInputs.setTargetItem(targetItem)

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier,
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
                    text = stringResource(R.string.move_folder_title),
                )

                MoveFolderList(
                    modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
                    folders = moveFolderDialogState.folders,
                    targetFolderId = moveFolderDialogState.targetFolderId,
                    onSelect = moveFolderDialogInputs::selectTargetFolderId
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    MoveDialogCloseButton(
                        modifier = modifier,
                        buttonLabel = stringResource(R.string.move_folder_negative),
                        onClick = onDismissRequest
                    )
                    Spacer(modifier = modifier.size(12.dp))
                    MoveDialogConfirmButton(
                        modifier = modifier,
                        buttonLabel = stringResource(R.string.move_folder_positive),
                        onClick = {
                            moveFolderDialogInputs.moveFolder()
                            onDismissRequest()
                        },
                        enabled = moveFolderDialogState.enableConfirmButton
                    )
                }
            }
        }
    }
}

@Composable
fun MoveFolderList(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    targetFolderId: Int,
    onSelect: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = folders, key = { it.id }) {
            MoveFolderItem(
                folder = it,
                selected = (targetFolderId == it.id),
                onSelect = onSelect
            )
        }
    }
}

@Composable
fun MoveFolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    selected: Boolean,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(4.dp)
            .clickable { onSelect(folder.id) },
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
                    painter = painterResource(id = R.drawable.icon_select_full),
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
fun MoveDialogCloseButton(
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
fun MoveDialogConfirmButton(
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


