package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sparta.imagesearch.domain.FolderId
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
    var folders by remember {
        mutableStateOf<List<Folder>>(emptyList())
    }
    var selectedFolderId by remember {
        mutableStateOf(FolderId.DEFAULT_FOLDER.id)
    }
    var itemsInFolder by remember {
        mutableStateOf<List<Item>>(emptyList())
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var targetItem by remember { mutableStateOf<Item?>(null) }
    var showMoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.folders.collect {
            folders = it
        }
    }
    LaunchedEffect(Unit) {
        viewModel.selectedFolderId.collect {
            selectedFolderId = it
        }
    }
    LaunchedEffect(Unit) {
        viewModel.itemsInFolder.collect {
            itemsInFolder = it
        }
    }

    LaunchedEffect(selectedFolderId) {
        viewModel.loadItemsInFolder()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FolderListContent(
                modifier = modifier.fillMaxHeight(0.1f),
                folders = folders,
                selectedFolderId = selectedFolderId,
                onFolderClick = viewModel::selectFolder,
                onAddClick = { showAddDialog = true },
                onDeleteClick = { showDeleteDialog = true }
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
                folderItems = itemsInFolder,
                onHeartClick = viewModel::unSaveItem,
                onHeartLongClick = {
                    showMoveDialog = true
                    targetItem = it
                }
            )
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = showAddDialog
            ) {
                AddFolderDialog(
                    onDismissRequest = { showAddDialog = false },
                    addFolder = viewModel::addFolder
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = showDeleteDialog
            ) {
                DeleteFolderDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    folders = folders,
                    deleteFolder = viewModel::deleteFolders
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(1f),
                visible = showMoveDialog
            ) {
                MoveFolderDialog(
                    folders = folders,
                    curFolderId = selectedFolderId,
                    onDismissRequest = { showMoveDialog = false },
                    moveFolder = { folderId -> viewModel.moveFolder(targetItem!!, folderId) }
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
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            FolderList(
                modifier = modifier.fillMaxWidth(0.9f),
                folders = folders,
                isSelected = {folderId -> selectedFolderId == folderId},
                onFolderClick = onFolderClick
            )
            FolderDropDown(
                modifier = modifier
                    .fillMaxWidth(),
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
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
fun Folder(
    modifier: Modifier = Modifier,
    folder: Folder,
    isSelected: Boolean
) {
    Column(
        modifier = modifier.height(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = modifier.scale(1.3f).padding(bottom = 4.dp),
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
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Image(
                modifier = modifier
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