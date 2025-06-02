package com.example.pix.ui.image

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.pix.R
import com.example.pix.ui.ImagesViewModel

@Composable
fun ImageScreen(
    navController: NavController,
    viewModel: ImagesViewModel,
) {
    val maxScale = 4f
    val minScale = 1f

    val state by viewModel.currentState.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = state.selectedPictureIndex,
        pageCount = {
            state.pictures.size
        }
    )

    var scale by remember { mutableFloatStateOf(minScale) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    var alpha by remember { mutableFloatStateOf(0f) }

    val animatedScale by animateFloatAsState(
        targetValue = scale
    )
    val animatedOffset by animateOffsetAsState(
        targetValue = offset
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha
    )

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(minScale, maxScale)

                            val extraWidth = (scale - 1) * imageSize.width
                            val extraHeight = (scale - 1) * imageSize.height
                            val maxX = extraWidth / 2
                            val maxY = extraHeight / 2

                            offset = Offset(
                                x = (offset.x + pan.x).coerceIn(-maxX, maxX),
                                y = (offset.y + pan.y).coerceIn(-maxY, maxY)
                            )
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale != minScale) {
                                    scale = minScale
                                    offset = Offset.Zero
                                } else
                                    scale = maxScale
                            }
                        )
                    }
                    .graphicsLayer(
                        scaleX = animatedScale,
                        scaleY = animatedScale,
                        translationX = animatedOffset.x,
                        translationY = animatedOffset.y
                    ),
                key = { state.pictures[it].id },
                beyondViewportPageCount = 1
            ) { newIndex ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = state.pictures[newIndex].highQualityUrl,
                        contentDescription = null,
                        alpha = animatedAlpha,
                        error = painterResource(R.drawable.img_error),
                        onLoading = {
                            offset = Offset.Zero
                            scale = 1f
                            alpha = 0f
                        },
                        onSuccess = {
                            alpha = 1f
                            imageSize = it.painter.intrinsicSize
                        },
                        onError = {
                            alpha = 1f
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = state.pictures[newIndex].title,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                all = dimensionResource(R.dimen.dimen_picture_title)
                            )
                    )
                }
            }
            Button(
                onClick = {
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = paddingValues,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    }
}