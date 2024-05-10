package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Folder
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.BottomNavItem
import com.sparta.imagesearch.presentation.ImageSearchBottomNavBar
import com.sparta.imagesearch.presentation.ImageSearchItem


@Composable
fun FolderScreen(
    modifier: Modifier = Modifier,
    viewModel: FolderViewModel = hiltViewModel(),
    navToSearch: () -> Unit
) {
    val folderScreenState by viewModel.state.collectAsState(initial = FolderScreenState())
    val folderScreenInputs = viewModel.inputs

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FolderListContent(
                modifier = modifier.fillMaxHeight(0.1f),
                folders = folderScreenState.folders,
                selectedFolderId = folderScreenState.selectedFolderId,
                onFolderClick = folderScreenInputs::selectFolder,
                onAddClick = folderScreenInputs::openAddDialog,
                onDeleteClick = folderScreenInputs::openDeleteDialog
            )
        },
        bottomBar = {
            ImageSearchBottomNavBar(
                selectedNavItem = BottomNavItem.Folder,
                onNavItemClick = navToSearch
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            FolderItemsContent(
                modifier = modifier.fillMaxHeight(),
                folderItems = folderScreenState.savedItemsInFolder,
                onHeartClick = folderScreenInputs::unSaveItem,
                onHeartLongClick = folderScreenInputs::openMoveDialog
            )
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = folderScreenState.showAddDialog
            ) {
                AddFolderDialog(
                    onDismissRequest = folderScreenInputs::closeAddDialog
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = folderScreenState.showDeleteDialog
            ) {
                DeleteFolderDialog(
                    onDismissRequest = folderScreenInputs::closeDeleteDialog
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = folderScreenState.showMoveDialog
            ) {
                MoveFolderDialog(
                    targetItem = folderScreenState.targetItem,
                    onDismissRequest = folderScreenInputs::closeMoveDialog,
                )
            }
        }
    }
}

@Composable
fun FolderListContent(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    selectedFolderId: Int,
    onFolderClick: (folder: Folder) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FolderList(
                modifier = modifier.fillMaxWidth(0.85f),
                folders = folders,
                isSelected = { folderId -> selectedFolderId == folderId },
                onFolderClick = onFolderClick
            )
            FolderDropDown(
                modifier = modifier.fillMaxWidth(),
                onAddClick = onAddClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun FolderList(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    isSelected: (Int) -> Boolean,
    onFolderClick: (folder: Folder) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        LazyRow(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(items = folders, key = { it.id }) {
                Folder(
                    modifier = Modifier.clickable { onFolderClick(it) },
                    folder = it,
                    isSelected = isSelected(it.id)
                )
            }
        }
    }
}

@Composable
fun Folder(
    modifier: Modifier = Modifier,
    folder: Folder,
    isSelected: Boolean
) {
    val dotAlpha = animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.0f, label = "dot_alpha"
    )

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = modifier.scale(1.2f),
            painter = painterResource(id = R.drawable.icon_folder),
            colorFilter = ColorFilter.tint(Color(parseColor(folder.colorHex))),
            contentDescription = ""
        )
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = folder.name
        )
        Image(
            modifier = modifier
                .alpha(dotAlpha.value)
                .fillMaxWidth()
                .padding(4.dp)
                .scale(1.5f)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.icon_dot),
            colorFilter = ColorFilter.tint(Color.DarkGray),
            contentDescription = ""
        )
    }
}

@Composable
fun FolderDropDown(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FolderDropDownIcon(
            modifier = modifier.align(Alignment.Center),
            onClick = { expanded = true }
        )

        DropdownMenu(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Text(
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .clickable {
                        onAddClick()
                        expanded = false
                    },
                text = context.getString(R.string.more_add_folder)
            )
            Text(
                modifier = modifier
                    .clickable {
                        onDeleteClick()
                        expanded = false
                    },
                text = context.getString(R.string.more_delete_folder)
            )
        }
    }
}

@Composable
fun FolderDropDownIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        modifier = modifier
            .padding(1.dp)
            .clickable { onClick() },
        painter = painterResource(id = R.drawable.icon_more),
        colorFilter = ColorFilter.tint(Color.DarkGray),
        contentDescription = ""
    )
}


@Composable
fun FolderItemsContent(
    modifier: Modifier = Modifier,
    folderItems: List<Item>,
    onHeartClick: (item: Item) -> Unit,
    onHeartLongClick: (item: Item) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(items = folderItems, key = { it.imageUrl }) { item ->
                ImageSearchItem(
                    item = item,
                    onHeartClick = { onHeartClick(item) },
                    onHeartLongClick = { onHeartLongClick(item) }
                )
            }
        }
    }
}