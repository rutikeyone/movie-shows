package com.ru.movieshows.data.genres.di

import com.ru.movieshows.data.genres.api.GenresApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class GenresApiModule {

    @Provides
    fun provideGenresApi(retrofit: Retrofit): GenresApi {
        return retrofit.create(GenresApi::class.java)
    }

}