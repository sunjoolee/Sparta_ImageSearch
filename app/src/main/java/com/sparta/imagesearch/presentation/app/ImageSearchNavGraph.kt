package com.sparta.imagesearch.presentation.app

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sparta.imagesearch.presentation.folder.FolderScreen
import com.sparta.imagesearch.presentation.search.SearchScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ImageSearchNavGraph(
    navGraphState: ImageSearchNavGraphState = rememberImageSearchNavGraphState()
) {
    var curDestinationState by remember{ mutableStateOf(ImageSearchScreen.SEARCH_SCREEN.route) }

    LaunchedEffect(Unit) {
        navGraphState.navController.currentBackStackEntryFlow.collectLatest { entry ->
            entry.destination.route?.let {
                Log.d("ImageSearchNavGraph", "collectLatest) curDestination: $it")
                curDestinationState = it
            }
        }
    }

    Scaffold(
        bottomBar = {
            ImageSearchBottomNavBar(
                curDestination = curDestinationState,
                navigationActions = navGraphState.navActions
            )
        }
    )
    {
        Surface(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navGraphState.navController,
                startDestination = navGraphState.startDestination
            ) {
                composable(ImageSearchScreen.SEARCH_SCREEN.route) {
                    SearchScreen()
                }
                composable(ImageSearchScreen.FOLDER_SCREEN.route) {
                    FolderScreen()
                }
            }
        }
    }
}

@Composable
fun rememberImageSearchNavGraphState(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ImageSearchScreen.SEARCH_SCREEN.route,
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