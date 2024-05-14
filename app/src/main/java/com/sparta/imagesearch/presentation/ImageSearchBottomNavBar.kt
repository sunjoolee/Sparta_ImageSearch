package com.sparta.imagesearch.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme


@Composable
fun ImageSearchBottomNavBar(
    bottomNavItems: List<BottomNavItem> = listOf(BottomNavItem.Search, BottomNavItem.Folder),
    selectedNavItem: BottomNavItem,
    onNavItemClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.scheme.surface,
        contentColor = MaterialTheme.scheme.onSurface
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bottomNavItems.forEach { navItem ->
                ImageSearchBottomNavItem(
                    iconId = navItem.iconId,
                    labelId = navItem.labelId,
                    selected = (selectedNavItem == navItem),
                    onClick = {
                        if (navItem != selectedNavItem) onNavItemClick()
                    }
                )
            }
        }
    }
}

@Composable
fun ImageSearchBottomNavItem(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = Padding.medium)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(bottom = Padding.small),
            painter = painterResource(id = iconId),
            colorFilter = ColorFilter.tint(
                if (selected) MaterialTheme.scheme.tertiary
                else MaterialTheme.scheme.disabled
            ),
            contentDescription = ""
        )
        Text(
            color =
            if (selected) MaterialTheme.scheme.onSurface
            else MaterialTheme.scheme.disabled,
            text = stringResource(id = labelId)
        )
    }
}

sealed class BottomNavItem(
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int
) {
    data object Search : BottomNavItem(R.drawable.icon_search, R.string.menu_search)
    data object Folder : BottomNavItem(R.drawable.icon_folder, R.string.menu_folder)
}
