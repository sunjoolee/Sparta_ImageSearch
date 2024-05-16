package com.sparta.imagesearch.presentation.app

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    var curDestinationState by remember { mutableStateOf(ImageSearchScreen.SEARCH_SCREEN.route) }

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
                startDestination = navGraphState.startDestination,
            ) {
                composable(
                    route = ImageSearchScreen.SEARCH_SCREEN.route,
                    enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
                    },
                    exitTransition = {
                        fadeOut(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    }
                ) {
                    SearchScreen()
                }
                composable(
                    route = ImageSearchScreen.FOLDER_SCREEN.route,
                    enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                    exitTransition = {
                        fadeOut(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.End
                        )
                    }
                ) {
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