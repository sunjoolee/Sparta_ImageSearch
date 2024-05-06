package com.sparta.imagesearch.presentation.search

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.source.local.folder.FolderColor
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.domain.Item
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(modelClass = SearchViewModel::class.java)
) {
    val pagingItems = viewModel.itemPagingFlow.collectAsLazyPagingItems()

    val (keywordState, setKeywordState) = remember { mutableStateOf("") }

    LaunchedEffect(viewModel.keyword) {
        viewModel.keyword.collect {
            Log.d("SearchScreen", "LaunchedEffect) keyword: $it")
            setKeywordState(it)
            pagingItems.refresh()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageSearchBar(
            query = keywordState,
            setQuery = setKeywordState,
            onSearch = viewModel::search
        )
        ResultItemsContent(
            pagingItems = pagingItems,
            onHeartClick = viewModel::saveItem
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    setQuery: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var (searchBarActive, setSearchBarActive) = remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        query = query,
        onQueryChange = setQuery,
        onSearch = {
            onSearch(it)
            setSearchBarActive(false)
        },
        active = searchBarActive,
        onActiveChange = setSearchBarActive,
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
    Text(
        modifier = modifier,
        text = stringResource(R.string.search_hint)
    )
}

@Composable
fun ResultItemsContent(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<Item>,
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
            if (pagingItems.loadState.refresh is LoadState.Loading) {
                Log.d("SearchScreen", "ResultItemsContent: refresh & loading")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalStaggeredGrid(
                    state = gridState,
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(count = pagingItems.itemCount) { index ->
                        pagingItems[index]?.let {
                            ImageSearchItem(
                                item = it,
                                onHeartClick = onHeartClick
                            )
                        }
                    }
                    item {
                        if (pagingItems.loadState.append is LoadState.Loading) {
                            Log.d("SearchScreen", "ResultItemsContent: append & loading")
                            CircularProgressIndicator()
                        }
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
}


@Composable
fun ImageSearchItem(
    modifier: Modifier = Modifier,
    item: Item,
    onHeartClick: (item: Item) -> Unit = {},
    onHeartLongClick: (item: Item) -> Unit = {}
) {
    Card {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                ItemImage(
                    modifier = modifier,
                    imageUrl = item.imageUrl
                )
                ItemHeart(
                    modifier = modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    folderId = item.folderId,
                    onHeartClick = { onHeartClick(item) },
                    onHeartLongClick = { onHeartLongClick(item) })
            }
            Column(
                modifier = modifier
                    .padding(8.dp)
            ) {
                Text(text = item.time)
                Text(text = item.source)
            }
        }
    }
}


@Composable
fun ItemImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf<GlideImageState>(GlideImageState.None) }

    if (state is GlideImageState.None || state is GlideImageState.Loading) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .size(160.dp)
                .background(brush = shimmerBrush())
        )
    }

    GlideImage(
        modifier = modifier,
        imageModel = { imageUrl },
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillWidth
        ),
        onImageStateChanged = {
            state = it
        }
    )
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.8f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.8f),
        )

        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ),
            label = "shimmer"
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemHeart(
    folderId: String,
    modifier: Modifier = Modifier,
    onHeartClick: () -> Unit,
    onHeartLongClick: () -> Unit = {}
) {
    val heartColor = animateColorAsState(
        targetValue = Color(
            parseColor(
                if (folderId != FolderId.NO_FOLDER.id) FolderColor.color1.colorHex
                else (FolderColor.color0.colorHex)
            )
        ),
        label = "heart_color"
    )

    Image(
        painter = painterResource(id = R.drawable.icon_heart),
        colorFilter = ColorFilter.tint(color = heartColor.value),
        contentDescription = "",
        modifier = modifier
            .scale(1.5f)
            .combinedClickable(
                onClick = onHeartClick,
                onLongClick = onHeartLongClick
            )
    )
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


