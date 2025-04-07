package com.example.pix.ui.imagesList

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.pix.data.flickr.FlickrRepository
import com.example.pix.data.room.PictureDatabase
import com.example.pix.domain.entity.Picture
import com.example.pix.domain.useCase.LoadPicturesUseCase
import kotlinx.coroutines.launch

class ImagesListViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    data class ImagesListState(
        val pictures: List<Picture> = listOf(),
    )

    private val loadPicturesUseCase = LoadPicturesUseCase(
        FlickrRepository(
            Room.databaseBuilder(
                application.baseContext,
                PictureDatabase::class.java,
                "picture_database"
            )
                .build()
        )
    )
    private val mutableCurrentList = MutableLiveData(ImagesListState())
    val currentList: LiveData<ImagesListState> get() = mutableCurrentList

    fun searchPictures(text: String) {
        viewModelScope.launch {
            val data = loadPicturesUseCase.searchPictures(text)

            if (data == null)
                Toast.makeText(
                    application.baseContext,
                    FlickrRepository.ERROR_TEXT,
                    Toast.LENGTH_SHORT
                ).show()
            else
                mutableCurrentList.value = currentList.value?.copy(pictures = data)
        }
    }

    fun loadRecentPictures(picturesList: List<Picture>) {
        if (picturesList.isNotEmpty())
            mutableCurrentList.value = currentList.value?.copy(pictures = picturesList)
        else
            viewModelScope.launch {
                val cachedData = loadPicturesUseCase.getCachedPictures()
                mutableCurrentList.value = currentList.value?.copy(pictures = cachedData)

                launch {
                    val data = loadPicturesUseCase.recentPictures()

                    if (data == null)
                        Toast.makeText(
                            application.baseContext,
                            FlickrRepository.ERROR_TEXT,
                            Toast.LENGTH_SHORT
                        ).show()
                    else {
                        mutableCurrentList.value = currentList.value?.copy(pictures = data)
                        loadPicturesUseCase.clearCache()
                        loadPicturesUseCase.cachePictures(data)
                    }
                }
            }
    }
}