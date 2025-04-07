package com.example.pix.domain.useCase

import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repositoryInterface.FlickrRepositoryInterface

class LoadPicturesUseCase(private val repository: FlickrRepositoryInterface) {
    suspend fun searchPictures(text: String, page: Int = 1, count: Int = 100) =
        repository.search(text, page, count)

    suspend fun recentPictures() = repository.recentPictures()
    suspend fun getCachedPictures() = repository.getCachedPictures()
    suspend fun cachePictures(pictures: List<Picture>) = repository.cachePictures(pictures)
    suspend fun clearCache() = repository.clearCachedPictures()
}