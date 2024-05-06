package com.sparta.imagesearch.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.folder.FolderScreen
import com.sparta.imagesearch.presentation.search.SearchScreen

sealed class NavScreen(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int
) {
    data object Search : NavScreen(R.drawable.icon_search, R.string.menu_search)
    data object Folder : NavScreen(R.drawable.icon_folder, R.string.menu_folder)
}


@Composable
fun ImageSearchApp() {

    val navScreens = listOf(NavScreen.Search, NavScreen.Folder)
    var selectedScreen by remember { mutableStateOf(navScreens[0]) }

    Surface {
        Scaffold(
            bottomBar = {
                MainNavigationBar(
                    navScreens = navScreens,
                    selectedNavScreen = selectedScreen,
                    onNavScreenSelected = { selectedScreen = it }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when(selectedScreen) {
                    NavScreen.Search -> SearchScreen()
                    NavScreen.Folder -> FolderScreen()
                }
            }
        }
    }
}

@Composable
fun MainNavigationBar(
    navScreens: List<NavScreen>,
    selectedNavScreen: NavScreen = navScreens[0],
    onNavScreenSelected: (NavScreen) -> Unit = {}
) {
    NavigationBar {
        navScreens.forEach { navScreen ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = navScreen.iconRes),
                        contentDescription = ""
                    )
                },
                label = { Text(text = stringResource(navScreen.textRes)) },
                selected = (selectedNavScreen == navScreen),
                onClick = { onNavScreenSelected(navScreen) }
            )
        }
    }
}