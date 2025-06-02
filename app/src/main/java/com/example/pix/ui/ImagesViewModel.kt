package com.example.pix.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pix.data.flickr.FlickrRepository
import com.example.pix.domain.useCase.LoadPicturesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCase: LoadPicturesUseCase,
) : ViewModel() {

    private val _currentState = MutableStateFlow(ImagesUiState())
    val currentState: StateFlow<ImagesUiState> = _currentState.asStateFlow()

    init {
        loadRecentPictures()
    }

    fun searchPictures() {
        viewModelScope.launch {
            _currentState.update {
                it.copy(
                    pictures = listOf()
                )
            }
            if (currentState.value.searchText.isBlank())
                loadRecentPictures()
            else {
                val data = useCase.searchPictures(currentState.value.searchText)

                if (data == null)
                    Toast.makeText(
                        context,
                        FlickrRepository.ERROR_TEXT,
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    _currentState.update {
                        it.copy(
                            pictures = data
                        )
                    }
            }
        }
    }

    private fun loadRecentPictures() {
        viewModelScope.launch {
            val cachedData = useCase.getCachedPictures()
            _currentState.update {
                it.copy(
                    pictures = cachedData
                )
            }

            launch {
                val data = useCase.recentPictures()

                if (data == null)
                    Toast.makeText(
                        context,
                        FlickrRepository.ERROR_TEXT,
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    _currentState.update {
                        it.copy(
                            pictures = data
                        )
                    }
                    useCase.clearCache()
                    useCase.cachePictures(data)
                }
            }
        }
    }

    fun onSearchTextChange(newText: String) {
        _currentState.update {
            it.copy(
                searchText = newText
            )
        }
    }

    fun onPictureClick(index: Int) {
        _currentState.update {
            it.copy(
                selectedPictureIndex = index
            )
        }
    }
}