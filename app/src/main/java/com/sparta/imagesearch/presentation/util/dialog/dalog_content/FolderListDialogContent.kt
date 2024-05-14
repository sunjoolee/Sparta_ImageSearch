package com.sparta.imagesearch.presentation.util.dialog.dalog_content

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.FolderId
import com.sparta.imagesearch.presentation.theme.ImageSearchColorScheme
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.util.dialog.FOLDER_ITEM_HEIGHT_DP
import com.sparta.imagesearch.presentation.util.dialog.FOLDER_SELECT_ICON_SCALE
import com.sparta.imagesearch.presentation.util.hexToColor

@Composable
fun FolderListDialogContent(
    folders: List<Folder>,
    selectedFolderIds: List<Int>,
    @DrawableRes selectedIconId: Int,
    onFolderSelectById: (Int) -> Unit
){
    val context = LocalContext.current

    LazyColumn{
        items(items = folders, key = { it.id }) {
            FolderItem(
                folder = it,
                folderSelected = selectedFolderIds.contains(it.id),
                selectedIconId = selectedIconId,
                onFolderSelectById = {selectedFolderId ->
                    if (selectedFolderId == FolderId.DEFAULT_FOLDER.id)
                        Toast.makeText(
                            context,
                            context.getString(R.string.delete_folder_default_toast),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    else onFolderSelectById(selectedFolderId)
                }
            )
        }
    }
}

@Composable
fun FolderItem(
    folder: Folder,
    folderSelected: Boolean,
    @DrawableRes selectedIconId: Int,
    onFolderSelectById: (Int) -> Unit
) {
    Card(
        modifier = Modifier.padding(bottom = Padding.medium),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ImageSearchColorScheme.defaultScheme.surface,
            contentColor = ImageSearchColorScheme.defaultScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FOLDER_ITEM_HEIGHT_DP)
                .padding(Padding.medium)
                .clickable { onFolderSelectById(folder.id) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Spacer(modifier = Modifier.padding(start = Padding.default))
            if (folderSelected) FolderSelectIcon(selectedIconId) else FolderSelectIcon()
            Spacer(modifier = Modifier.padding(start = Padding.default))
            Image(
                painter = painterResource(id = R.drawable.icon_folder),
                colorFilter = ColorFilter.tint(folder.colorHex.hexToColor()),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(start = Padding.medium))
            Text(text = folder.name)
        }
    }
}

@Composable
fun FolderSelectIcon(
    @DrawableRes iconId: Int = R.drawable.icon_select_empty
){
    Image(
        modifier = Modifier.scale(FOLDER_SELECT_ICON_SCALE),
        painter = painterResource(id = iconId),
        contentDescription = ""
    )
}
