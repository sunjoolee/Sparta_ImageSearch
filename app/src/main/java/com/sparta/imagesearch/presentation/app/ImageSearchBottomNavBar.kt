package com.sparta.imagesearch.presentation.app

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme

private val BOTTOM_NAV_ITEM_IMAGE_SCALE = 1.2f

@Composable
fun ImageSearchBottomNavBar(
    curDestination: String,
    navigationActions: ImageSearchNavigationActions
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.scheme.surface,
        contentColor = MaterialTheme.scheme.onSurface
    ) {
        Row(
            modifier = Modifier.padding(vertical = Padding.medium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            with(ImageSearchScreen.SEARCH_SCREEN) {
                ImageSearchBottomNavItem(
                    iconId = bottomNavIconId,
                    labelId = bottomNavLabelId,
                    selected = (route == curDestination),
                    onClick = navigationActions::navigateToSearch
                )
            }
            with(ImageSearchScreen.FOLDER_SCREEN) {
                ImageSearchBottomNavItem(
                    iconId = bottomNavIconId,
                    labelId = bottomNavLabelId,
                    selected = (route == curDestination),
                    onClick = navigationActions::navigateToFolder
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
            .padding(Padding.medium)
            .clickable(
                onClick = { if(!selected) onClick() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .scale(BOTTOM_NAV_ITEM_IMAGE_SCALE)
                .padding(bottom = Padding.medium),
            painter = painterResource(id = iconId),
            colorFilter = ColorFilter.tint(
                if (selected) MaterialTheme.scheme.tertiary
                else MaterialTheme.scheme.disabled
            ),
            contentDescription = ""
        )
        Text(
            color =
            if (selected) MaterialTheme.scheme.onSurface else MaterialTheme.scheme.disabled,
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


