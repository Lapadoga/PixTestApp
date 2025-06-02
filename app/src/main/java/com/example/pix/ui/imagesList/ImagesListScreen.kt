package com.example.pix.ui.imagesList

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pix.R
import com.example.pix.ui.ImagesViewModel
import com.example.pix.ui.navigation.NavScreens

@Composable
fun ImagesListScreen(
    navController: NavController,
    viewModel: ImagesViewModel,
) {
    val state by viewModel.currentState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                value = state.searchText,
                onValueChange = {
                    viewModel.onSearchTextChange(it)
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_custom),
                        contentDescription = null,
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint)
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.searchPictures()
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(
                    integerResource(R.integer.int_column_count)
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(items = state.pictures) { index, item ->
                    var alpha by remember { mutableFloatStateOf(0f) }
                    val animatedAlpha by animateFloatAsState(
                        targetValue = alpha
                    )
                    AsyncImage(
                        model = item.lowQualityUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alpha = animatedAlpha,
                        placeholder = painterResource(R.drawable.img_loading_placeholder),
                        error = painterResource(R.drawable.img_error),
                        onSuccess = {
                            alpha = 1f
                        },
                        onError = {
                            alpha = 1f
                        },
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.dimen_item_image_margin))
                            .clickable {
                                viewModel.onPictureClick(index)
                                navController.navigate(route = NavScreens.PictureScreen.route)
                            }
                    )
                }
            }
        }
    }
}