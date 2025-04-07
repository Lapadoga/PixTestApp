package com.example.pix.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pix.domain.entity.Picture

class ImageViewModel : ViewModel() {
    data class ImageState(
        val picture: Picture? = null
    )

    private val mutableCurrentImage = MutableLiveData(ImageState())
    val currentImage: LiveData<ImageState> get() = mutableCurrentImage

    fun loadPicture(picture: Picture) {
        val pictureUrl = picture.url
        val urlParts = pictureUrl.split('/').toMutableList()
        val imagePathParts = urlParts[urlParts.size - 1].split('_').toMutableList()
        imagePathParts[imagePathParts.size - 1] = "b.jpg"
        urlParts[urlParts.size - 1] = imagePathParts.joinToString("_")
        val newPicture = Picture(picture.id, urlParts.joinToString("/"), picture.title)
        mutableCurrentImage.value = currentImage.value?.copy(picture = newPicture)
    }
}