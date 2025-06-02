package com.example.pix.ui

import androidx.compose.runtime.Immutable
import com.example.pix.domain.entity.Picture

@Immutable
data class ImagesUiState(
    val pictures: List<Picture> = listOf(),
    val searchText: String = "",
    val selectedPictureIndex: Int = -1,
)