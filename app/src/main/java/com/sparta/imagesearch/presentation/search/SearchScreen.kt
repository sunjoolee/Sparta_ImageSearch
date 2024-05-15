package com.sparta.imagesearch.presentation.search

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.BottomNavItem
import com.sparta.imagesearch.presentation.ImageSearchBottomNavBar
import com.sparta.imagesearch.presentation.ImageSearchItem
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val GRID_CELL_MIN_SIZE = 128.dp
val GRID_CELL_FIZED = 2
val SEARCH_BAR_ICON_SCALE = 1.5f
val FAB_ICON_SIZE = 20.dp

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navToFolder: () -> Unit
) {
    val searchScreenState by viewModel.state.collectAsState(initial = SearchScreenState())
    val searchScreenInputs = viewModel.inputs

    val gridCellConfiguration =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            StaggeredGridCells.Adaptive(minSize = GRID_CELL_MIN_SIZE)
        } else {
            StaggeredGridCells.Fixed(GRID_CELL_FIZED)
        }
    val gridState = rememberLazyStaggeredGridState()
    val showScrollToTopFab by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.scheme.background,
        contentColor = MaterialTheme.scheme.onBackground,
        bottomBar = {
            ImageSearchBottomNavBar(
                selectedNavItem = BottomNavItem.Search,
                onNavItemClick = navToFolder
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                modifier = modifier.padding(Padding.default),
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
                modifier = modifier.fillMaxWidth(),
                keyword = searchScreenState.keyword,
                onSearch = searchScreenInputs::updateKeyword
            )
            ResultItemsContent(
                modifier = modifier.padding(top = Padding.medium),
                gridCells = gridCellConfiguration,
                gridState = gridState,
                resultItems = searchScreenState.resultItems,
                folderColorHexById = searchScreenInputs::getFolderColorHexById,
                onHeartClick = searchScreenInputs::saveItem
            )
        }
    }
}


@Composable
fun ImageSearchBar(
    modifier: Modifier = Modifier,
    keyword: String,
    onSearch: (String) -> Unit
) {
    var textFieldValue  = rememberSaveable(keyword) {
        TextFieldValue(text = keyword, selection = TextRange(keyword.length))
    }

    TextField(
        modifier = modifier.background(Color.Transparent),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(textFieldValue.text) }),
        leadingIcon = {
            Image(
                modifier = Modifier.scale(SEARCH_BAR_ICON_SCALE),
                painter = painterResource(id = R.drawable.icon_search),
                colorFilter = ColorFilter.tint(MaterialTheme.scheme.tertiary),
                contentDescription = ""
            )
        },
        trailingIcon = {
            SearchBarButton(
                onClick = { onSearch(textFieldValue.text) }
            )
        },
        placeholder = {
            Text(
                modifier = modifier,
                text = stringResource(R.string.search_hint),
                style = MaterialTheme.typography.bodySmall
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.scheme.onSurface,
            unfocusedTextColor = MaterialTheme.scheme.onSurface,
            cursorColor = MaterialTheme.scheme.tertiary
        )
    )
}

@Composable
fun SearchBarButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.scheme.tertiary,
            contentColor = MaterialTheme.scheme.onTertiary,
            disabledContainerColor = MaterialTheme.scheme.disabled,
            disabledContentColor = MaterialTheme.scheme.onDisabled
        )
    ) {
        Text(text = stringResource(id = R.string.search))
    }
}

@Composable
fun ResultItemsContent(
    modifier: Modifier = Modifier,
    gridCells: StaggeredGridCells,
    gridState: LazyStaggeredGridState,
    resultItems: List<Item>,
    folderColorHexById: (Int) -> String,
    onHeartClick: (item: Item) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            state = gridState,
            columns = gridCells,
            contentPadding = PaddingValues(Padding.default),
            horizontalArrangement = Arrangement.spacedBy(Padding.medium),
            verticalItemSpacing = Padding.medium
        ) {
            items(items = resultItems, key = { it.imageUrl }) {
                ImageSearchItem(
                    item = it,
                    heartColorHex = folderColorHexById(it.folderId),
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
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.scheme.tertiary,
                contentColor = MaterialTheme.scheme.onTertiary
            ),
            onClick = {
                scope.launch { gridState.animateScrollToItem(0) }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_up),
                contentDescription = "",
                modifier = Modifier
                    .padding(Padding.xSmall)
                    .size(FAB_ICON_SIZE)
            )
        }
    }
}

