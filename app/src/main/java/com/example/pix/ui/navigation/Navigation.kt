package com.example.pix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pix.ui.ImagesViewModel
import com.example.pix.ui.image.ImageScreen
import com.example.pix.ui.imagesList.ImagesListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val sharedViewModel: ImagesViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavScreens.ListScreen.route
    ) {
        composable(
            route = NavScreens.ListScreen.route
        ) {
            ImagesListScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
        composable(
            route = NavScreens.PictureScreen.route,
        ) {
            ImageScreen(
                navController = navController,
                viewModel = sharedViewModel
            )
        }
    }
}