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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.folder.FolderScreen
import com.sparta.imagesearch.presentation.search.SearchScreen

enum class NavigationItem(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int
) {
    NAV_ITEM_SEARCH(R.drawable.icon_search, R.string.menu_search),
    NAV_ITEM_FOLDER(R.drawable.icon_folder, R.string.menu_folder)
}

@Composable
fun ImageSearchApp() {
    var selectedNavItem by remember { mutableStateOf(NavigationItem.NAV_ITEM_SEARCH) }
    Surface {
        Scaffold(
            bottomBar = {
                MainNavigationBar(
                    onNavItemSelected = { navItem ->
                        selectedNavItem = navItem
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedNavItem) {
                    NavigationItem.NAV_ITEM_SEARCH -> SearchScreen()
                    NavigationItem.NAV_ITEM_FOLDER -> FolderScreen()
                }
            }
        }
    }
}

@Composable
fun MainNavigationBar(
    navItems: List<NavigationItem> = NavigationItem.entries,
    selectedNavItem: NavigationItem = NavigationItem.entries[0],
    onNavItemSelected: (NavigationItem) -> Unit
) {
    val context = LocalContext.current

    NavigationBar {
        navItems.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = navItem.iconRes),
                        contentDescription = "search"
                    )
                },
                label = { Text(text = context.getString(navItem.textRes)) },
                selected = (selectedNavItem == navItem),
                onClick = { onNavItemSelected(navItem) }
            )
        }
    }
}