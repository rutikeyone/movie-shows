package com.ru.movieshows.data.genres.di

import com.ru.movieshows.data.genres.service.GenresService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class GenresServiceModule {

    @Provides
    fun provideGenresService(retrofit: Retrofit): GenresService {
        return retrofit.create(GenresService::class.java)
    }

}