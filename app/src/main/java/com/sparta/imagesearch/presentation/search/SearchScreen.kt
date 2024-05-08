package com.sparta.imagesearch.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.BottomNavItem
import com.sparta.imagesearch.presentation.ImageSearchBottomNavBar
import com.sparta.imagesearch.presentation.ImageSearchItem
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

