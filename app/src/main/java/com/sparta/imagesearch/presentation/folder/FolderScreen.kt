package com.sparta.imagesearch.presentation.folder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.entity.Item
import com.sparta.imagesearch.presentation.search.ImageSearchItem

@Composable
fun FolderItemsContent(
    modifier: Modifier = Modifier,
    folderItems: List<Item>,
    onHeartClick: (item: Item) -> Unit,
    onHeartLongClick: (item:Item) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(items = folderItems, key = { it.id }) { item ->
                    ImageSearchItem(
                        item = item,
                        onHeartClick = {onHeartClick(item)},
                        onHeartLongClick = {onHeartLongClick(item)}
                    )
                }
            }
        }
    }
}