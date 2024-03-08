package com.ru.movieshows.data.movies.di

import com.ru.movieshows.data.MoviesDataRepository
import com.ru.movieshows.data.movies.MoviesDataRepositoryImpl
import dagger.Binds
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Singleton
@InstallIn(Singleton::class)
interface MoviesRepositoriesModule {

    @Binds
    fun provideMoviesRepository(
        moviesDataRepository: MoviesDataRepositoryImpl,
    ): MoviesDataRepository

}