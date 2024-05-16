package com.sparta.imagesearch.presentation.app

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.sparta.imagesearch.R

sealed class ImageSearchScreen {
    data object SEARCH_SCREEN {
        val route = "search_screen"
        @StringRes
        val bottomNavLabelId = R.string.menu_search
        @DrawableRes
        val bottomNavIconId = R.drawable.icon_search
    }
    data object FOLDER_SCREEN {
        val route = "folder_screen"
        @StringRes
        val bottomNavLabelId = R.string.menu_folder
        @DrawableRes
        val bottomNavIconId = R.drawable.icon_folder
    }
}

class ImageSearchNavigationActions(
    private val navController: NavController
) {
    fun navigateToSearch() {
        navController.navigate(ImageSearchScreen.SEARCH_SCREEN.route) {
            popUpTo(ImageSearchScreen.FOLDER_SCREEN.route) {
                inclusive = true
            }
        }
    }

    fun navigateToFolder() {
        navController.navigate(ImageSearchScreen.FOLDER_SCREEN.route) {
            popUpTo(ImageSearchScreen.SEARCH_SCREEN.route) {
                inclusive = true
            }
        }
    }
}