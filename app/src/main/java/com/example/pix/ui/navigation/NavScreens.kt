package com.example.pix.ui.navigation

sealed class NavScreens(val route: String) {
    data object ListScreen: NavScreens(LIST_SCREEN)
    data object PictureScreen: NavScreens(PICTURE_SCREEN)

    companion object {
        private const val LIST_SCREEN = "list_screen"
        private const val PICTURE_SCREEN = "picture_screen"
    }
}

