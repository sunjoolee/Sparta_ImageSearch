package com.sparta.imagesearch.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.R


@Composable
fun ImageSearchBottomNavBar(
    bottomNavItems: List<BottomNavItem> = listOf(BottomNavItem.Search, BottomNavItem.Folder),
    selectedNavItem: BottomNavItem,
    onNavItemClick: () -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = navItem.iconRes),
                        contentDescription = ""
                    )
                },
                label = { Text(text = stringResource(id = navItem.textRes)) },
                selected = (selectedNavItem == navItem),
                onClick = {
                    if(navItem != selectedNavItem) onNavItemClick()
                }
            )
        }
    }
}

sealed class BottomNavItem(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int
) {
    data object Search : BottomNavItem(R.drawable.icon_search, R.string.menu_search)
    data object Folder : BottomNavItem(R.drawable.icon_folder, R.string.menu_folder)
}
