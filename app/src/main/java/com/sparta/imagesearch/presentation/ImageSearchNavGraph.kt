package com.sparta.imagesearch.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sparta.imagesearch.presentation.folder.FolderScreen
import com.sparta.imagesearch.presentation.search.SearchScreen

@Composable
fun ImageSearchNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ImageSearchDestinations.SEARCH_ROUTE,
    navActions: ImageSearchNavigationActions = remember(navController) {
        ImageSearchNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ImageSearchDestinations.SEARCH_ROUTE) {
            SearchScreen(navToFolder = {navActions.navigateToFolder()})
        }
        composable(ImageSearchDestinations.FOLDER_ROUTE) {
            FolderScreen(navToSearch = {navActions.navigateToSearch()})
        }
    }
}