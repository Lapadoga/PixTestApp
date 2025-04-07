package com.example.pix.domain.repositoryInterface

import com.example.pix.domain.entity.Picture

interface FlickrRepositoryInterface {
    suspend fun search(text: String, page: Int, count: Int): List<Picture>?
    suspend fun recentPictures(): List<Picture>?
    suspend fun getCachedPictures(): List<Picture>
    suspend fun cachePictures(pictures: List<Picture>)
    suspend fun clearCachedPictures()
}