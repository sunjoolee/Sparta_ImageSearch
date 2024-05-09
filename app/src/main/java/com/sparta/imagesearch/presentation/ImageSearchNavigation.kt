package com.sparta.imagesearch.presentation

import androidx.navigation.NavController
import com.sparta.imagesearch.presentation.ImageSearchScreens.FOLDER_SCREEN
import com.sparta.imagesearch.presentation.ImageSearchScreens.SEARCH_SCREEN

private object ImageSearchScreens{
    const val SEARCH_SCREEN = "search_screen"
    const val FOLDER_SCREEN = "folder_screen"
}

object ImageSearchDestinations{
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val FOLDER_ROUTE = FOLDER_SCREEN
}

class ImageSearchNavigationActions(
    private val navController: NavController
){
    fun navigateToSearch(){
        navController.navigate(ImageSearchDestinations.SEARCH_ROUTE){
            popUpTo(ImageSearchDestinations.FOLDER_ROUTE){
                inclusive = true
            }
        }
    }
    fun navigateToFolder(){
        navController.navigate(ImageSearchDestinations.FOLDER_ROUTE){
            popUpTo(ImageSearchDestinations.SEARCH_ROUTE){
                inclusive = true
            }
        }
    }
}