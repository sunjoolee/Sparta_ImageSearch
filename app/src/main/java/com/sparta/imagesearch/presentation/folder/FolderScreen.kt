package com.sparta.imagesearch.presentation.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.sparta.imagesearch.presentation.theme.ImageSearchColorScheme
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.util.SelectIndicator
import com.sparta.imagesearch.presentation.util.hexToColor

val FOLDER_LIST_ITEM_IMAGE_SCALE = 1.2f
val FOLDER_DROP_DOWN_ICON_SCALE = 3.5f

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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            FolderListTopBar(
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
                folderItems = folderScreenState.selectedFolderItems,
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
fun FolderListTopBar(
    modifier: Modifier = Modifier,
    folders: List<Folder>,
    selectedFolderId: Int,
    onFolderClick: (folder: Folder) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .padding(horizontal = Padding.default)
            .padding(top = Padding.default)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FolderList(
                modifier = modifier.fillMaxWidth(0.9f),
                folders = folders,
                isSelected = { folderId -> selectedFolderId == folderId },
                onFolderClick = onFolderClick
            )
            Spacer(modifier = Modifier.padding(start = Padding.default))
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
        color = ImageSearchColorScheme.defaultScheme.surface,
        contentColor = ImageSearchColorScheme.defaultScheme.onSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        LazyRow(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Padding.default),
            contentPadding = PaddingValues(Padding.default)
        ) {
            items(items = folders, key = { it.id }) {
                FolderListItem(
                    modifier = Modifier.clickable { onFolderClick(it) },
                    folder = it,
                    isSelected = isSelected(it.id)
                )
            }
        }
    }
}

@Composable
fun FolderListItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    isSelected: Boolean
) {
    val indicatorAlpha = animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.0f, label = "dot_alpha"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = modifier.scale(FOLDER_LIST_ITEM_IMAGE_SCALE),
            painter = painterResource(id = R.drawable.icon_folder),
            colorFilter = ColorFilter.tint(folder.colorHex.hexToColor()),
            contentDescription = ""
        )
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = folder.name
        )
        SelectIndicator(alpha = indicatorAlpha.value)
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

    Box(
        modifier = modifier.fillMaxWidth().padding(top = Padding.large),
        contentAlignment = Alignment.Center,
    ) {
        FolderDropDownIcon(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = true }
        )
        DropdownMenu(
            modifier = Modifier
                .background(color = ImageSearchColorScheme.defaultScheme.dropdown),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Text(
                modifier = modifier
                    .padding(horizontal = Padding.medium)
                    .padding(vertical = Padding.small)
                    .clickable {
                        onAddClick()
                        expanded = false
                    },
                text = context.getString(R.string.more_add_folder)
            )
            Text(
                modifier = modifier
                    .padding(horizontal = Padding.medium)
                    .padding(bottom = Padding.small)
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
            .clickable { onClick() },
        painter = painterResource(id = R.drawable.icon_menu),
        colorFilter = ColorFilter.tint(ImageSearchColorScheme.defaultScheme.onDropDown),
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
        color = Color.Transparent,
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(Padding.default),
            horizontalArrangement = Arrangement.spacedBy(Padding.medium),
            verticalItemSpacing = Padding.medium
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