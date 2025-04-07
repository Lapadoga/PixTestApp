package com.example.pix.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Picture(
    val id: String,
    val url: String,
    val title: String,
)