package com.example.pix.data.flickr

import android.util.Log
import com.example.pix.data.flickr.mapper.toDboEntity
import com.example.pix.data.flickr.mapper.toEntity
import com.example.pix.data.room.PictureDao
import com.example.pix.data.room.PictureDbo
import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repositoryInterface.FlickrRepositoryInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FlickrRepository @Inject constructor(
    private val pictureDao: PictureDao,
    private val retrofit: FlickrApi,
    private val dispatcher: CoroutineContext
) : FlickrRepositoryInterface {

    override suspend fun search(
        text: String,
        page: Int,
        count: Int
    ): List<Picture>? {
        val searchResult = try {
            withContext(dispatcher) {
                retrofit.search(text, page, count)
            }
        } catch (e: Exception) {
            val message = e.message ?: ""
            Log.e("Image search request", message)
            null
        }

        var result: List<Picture>? = null
        if (searchResult != null) {
            if (searchResult.photos == null) {
                val message = searchResult.message ?: ""
                Log.e("Image search result", message)
            } else {
                val photos = searchResult.photos.photo
                result = mutableListOf()
                photos.forEach {
                    result.add(it.toEntity("q"))
                }
            }
        }

        return result
    }

    override suspend fun recentPictures(): List<Picture>? {
        val recentResult = try {
            withContext(dispatcher) {
                retrofit.getRecent()
            }
        } catch (e: Exception) {
            val message = e.message ?: ""
            Log.e("Recent images request", message)
            null
        }

        var result: List<Picture>? = null
        if (recentResult != null) {
            if (recentResult.photos == null) {
                val message = recentResult.message ?: ""
                Log.e("Recent images result", message)
            } else {
                val photos = recentResult.photos.photo
                result = mutableListOf()
                photos.forEach {
                    result.add(it.toEntity("q"))
                }
            }
        }

        return result
    }

    override suspend fun getCachedPictures(): List<Picture> {
        val result = mutableListOf<Picture>()
        val cachedPictures = pictureDao.getAll()
        cachedPictures.forEach {
            result.add(it.toEntity())
        }

        return result
    }

    override suspend fun cachePictures(pictures: List<Picture>) {
        val picturesToCache = mutableListOf<PictureDbo>()
        pictures.forEach {
            picturesToCache.add(it.toDboEntity())
        }
        pictureDao.insertAll(picturesToCache)
    }

    override suspend fun clearCachedPictures() {
        pictureDao.clearAll()
    }

    companion object {
        const val ERROR_TEXT = "Ошибка загрузки данных"
    }
}