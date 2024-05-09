package com.sparta.imagesearch.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sparta.imagesearch.presentation.folder.FolderScreen
import com.sparta.imagesearch.presentation.search.SearchScreen

@Composable
fun ImageSearchNavGraph(
    navGraphState: ImageSearchNavGraphState = rememberImageSearchNavGraphState()
) {
    NavHost(
        navController = navGraphState.navController,
        startDestination = navGraphState.startDestination
    ) {
        composable(ImageSearchDestinations.SEARCH_ROUTE) {
            SearchScreen(navToFolder = { navGraphState.navActions.navigateToFolder() })
        }
        composable(ImageSearchDestinations.FOLDER_ROUTE) {
            FolderScreen(navToSearch = { navGraphState.navActions.navigateToSearch() })
        }
    }
}

@Composable
fun rememberImageSearchNavGraphState(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ImageSearchDestinations.SEARCH_ROUTE,
    navActions: ImageSearchNavigationActions = remember(navController) {
        ImageSearchNavigationActions(navController)
    }
) = remember(navController, startDestination, navActions) {
    ImageSearchNavGraphState(navController, startDestination, navActions)
}

data class ImageSearchNavGraphState(
    val navController: NavHostController,
    val startDestination: String,
    val navActions: ImageSearchNavigationActions
)