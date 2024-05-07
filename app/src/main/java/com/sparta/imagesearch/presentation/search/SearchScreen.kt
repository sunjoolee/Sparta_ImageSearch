package com.sparta.imagesearch.presentation.search

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.source.local.folder.FolderColor
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.BottomNavItem
import com.sparta.imagesearch.presentation.ImageSearchBottomNavBar
import com.sparta.imagesearch.util.ShimmerBrush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navToFolder: () -> Unit
) {
    val (keywordState, setKeywordState) = remember {
        mutableStateOf("")
    }
    var searchResultItems by remember {
        mutableStateOf<List<Item>>(emptyList())
    }

    val gridState = rememberLazyStaggeredGridState()
    val showScrollToTopFab by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(Unit) {
        launch {
            viewModel.keyword.collect {
                setKeywordState(it)
                viewModel.search(keyword = it)
            }
        }
        launch {
            viewModel.resultItems.collect {
                searchResultItems = it
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            ImageSearchBottomNavBar(
                selectedNavItem = BottomNavItem.Search,
                onNavItemClick = navToFolder
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                modifier = modifier.padding(16.dp),
                visible = showScrollToTopFab
            ) {
                ScrollToTopFab(gridState = gridState)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            ImageSearchBar(
                onSearch = viewModel::search,
                query = keywordState,
                onQueryChange = setKeywordState,
            )
            ResultItemsContent(
                modifier = modifier.padding(top = 8.dp),
                gridState = gridState,
                resultItems = searchResultItems,
                onHeartClick = viewModel::saveItem
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val (searchBarActive, setSearchBarActive) = remember {
        mutableStateOf(false)
    }

    SearchBar(
        modifier = modifier
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {
            onSearch(it)
            setSearchBarActive(false)
        },
        active = searchBarActive,
        onActiveChange = setSearchBarActive,
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.icon_search),
                contentDescription = ""
            )
        },
        placeholder = {
            Text(
                modifier = modifier,
                text = stringResource(R.string.search_hint)
            )
        },
    ) {}
}

@Composable
fun ResultItemsContent(
    modifier: Modifier = Modifier,
    gridState: LazyStaggeredGridState,
    resultItems: List<Item>,
    onHeartClick: (item: Item) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            state = gridState,
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(items = resultItems, key = { it.imageUrl }) {
                ImageSearchItem(
                    item = it,
                    onHeartClick = onHeartClick
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
    val folderColor = remember{
        if(item.folderId == FolderId.NO_FOLDER.id) FolderColor.color0.colorHex
        else FolderColor.color1.colorHex
    }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                ItemImage(
                    modifier = modifier.align(Alignment.Center),
                    imageUrl = item.imageUrl
                )
                ItemHeart(
                    modifier = modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .scale(1.2f),
                    folderColor = folderColor,
                    onHeartClick = { onHeartClick(item) },
                    onHeartLongClick = { onHeartLongClick(item) })

            }
            Column(
                modifier = modifier.padding(8.dp)
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
                .background(brush = ShimmerBrush())
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemHeart(
    modifier: Modifier = Modifier,
    folderColor: String = FolderColor.color0.colorHex,
    onHeartClick: () -> Unit,
    onHeartLongClick: () -> Unit = {}
) {
    val heartColor = animateColorAsState(
        targetValue = Color(parseColor(folderColor)),
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
fun ScrollToTopFab(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    gridState: LazyStaggeredGridState
) {
    Box {
        Button(
            modifier = modifier,
            onClick = {
                scope.launch { gridState.animateScrollToItem(0) }
            }
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

