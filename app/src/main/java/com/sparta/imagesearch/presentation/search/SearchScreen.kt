package com.sparta.imagesearch.presentation.search

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.source.local.folder.FolderColor
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.entity.Item
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    var searchResultItems by remember {
        mutableStateOf<List<Item>>(emptyList())
    }
    LaunchedEffect(Unit) {
        viewModel.resultItems.collect {
            searchResultItems = it
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageSearchBar(
            onSearch = viewModel::search
        )
        ImageSearchResultContent(
            searchResultItems = searchResultItems,
            onHeartClick = viewModel::saveItem
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            onSearch(it)
            searchBarActive = false
        },
        active = searchBarActive,
        onActiveChange = { searchBarActive = it },
        leadingIcon = { ImageSearchBarLeadingIcon() },
        placeholder = { ImageSearchBarPlaceHolder() },

        ) {

    }
}

@Composable
fun ImageSearchBarLeadingIcon(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.icon_search),
        contentDescription = ""
    )
}

@Composable
fun ImageSearchBarPlaceHolder(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = context.getString(R.string.search_hint)
    )
}

@Composable
fun ImageSearchResultContent(
    modifier: Modifier = Modifier,
    searchResultItems: List<Item>,
    onHeartClick: (item: Item) -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()
    val showScrollButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            LazyVerticalStaggeredGrid(
                state = gridState,
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(items = searchResultItems, key = { it.id }) {
                    ImageSearchResultItem(
                        item = it,
                        onHeartClick = onHeartClick
                    )
                }
            }
            ScrollToTopButton(
                modifier = modifier.align(Alignment.BottomEnd),
                showScrollButton = showScrollButton,
                onClick = {
                    scope.launch { gridState.animateScrollToItem(0) }
                }
            )
        }
    }
}


@Composable
fun ScrollToTopButton(
    modifier: Modifier = Modifier,
    showScrollButton: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showScrollButton
    ) {
        Button(
            modifier = modifier.padding(8.dp),
            onClick = onClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_up),
                contentDescription = "",
                modifier = Modifier
                    .padding(2.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun ImageSearchResultItem(
    modifier: Modifier = Modifier,
    item: Item,
    onHeartClick: (item: Item) -> Unit
) {
    Card {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchResultImage(
                modifier = modifier,
                imageUrl = item.imageUrl
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SearchResultHeart(
                    modifier = modifier.align(Alignment.CenterEnd),
                    folderId = item.folderId,
                    onHeartClick = { onHeartClick(item) })
                Column(
                    modifier = modifier.align(Alignment.TopStart)
                ) {
                    Text(text = item.time)
                    Text(text = item.source)
                }
            }
        }
    }
}

@Composable
fun SearchResultImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf<GlideImageState>(GlideImageState.None) }
    if (state !is GlideImageState.Success) {
        CircularProgressIndicator(
            modifier = modifier.padding(10.dp)
        )
    }
    GlideImage(
        imageModel = { imageUrl },
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillWidth
        ),
        onImageStateChanged = { state = it }
    )
}

@Composable
fun SearchResultHeart(
    folderId: String,
    onHeartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartColor = animateColorAsState(
        targetValue = Color(
            parseColor(
                if (folderId != FolderId.NO_FOLDER.id) FolderColor.color1.colorHex
                else (FolderColor.color0.colorHex)
            )
        )
    )

    Image(
        painter = painterResource(id = R.drawable.icon_heart),
        colorFilter = ColorFilter.tint(color = heartColor.value),
        contentDescription = "",
        modifier = modifier
            .scale(1.5f)
            .clickable { onHeartClick() }
    )
}

