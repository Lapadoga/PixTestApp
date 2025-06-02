package com.example.pix.di

import android.content.Context
import androidx.room.Room
import com.example.pix.data.flickr.FlickrApi
import com.example.pix.data.flickr.FlickrRepository
import com.example.pix.data.room.PictureDao
import com.example.pix.data.room.PictureDatabase
import com.example.pix.domain.useCase.LoadPicturesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext = Dispatchers.IO

    @Provides
    @Singleton
    fun provideFlickrRetrofit(): FlickrApi = Retrofit.Builder()
        .baseUrl("https://www.flickr.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FlickrApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PictureDatabase =
        Room.databaseBuilder(
            context,
            PictureDatabase::class.java,
            "picture_database"
        )
            .build()

    @Provides
    @Singleton
    fun providePictureDao(database: PictureDatabase): PictureDao = database.getPictureDao()

    @Provides
    @Singleton
    fun provideLoadPicturesUseCase(repository: FlickrRepository) = LoadPicturesUseCase(repository)
}