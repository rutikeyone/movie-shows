package com.ru.movieshows.data.movies.di

import com.ru.movieshows.data.MoviesDataRepository
import com.ru.movieshows.data.movies.MoviesDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MoviesDataRepositoriesModule {

    @Binds
    fun provideMoviesDataRepository(
        moviesDataRepository: MoviesDataRepositoryImpl,
    ): MoviesDataRepository

}