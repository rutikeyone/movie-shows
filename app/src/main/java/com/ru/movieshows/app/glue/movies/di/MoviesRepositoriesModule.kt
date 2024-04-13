package com.ru.movieshows.app.glue.movies.di

import com.ru.movieshows.app.glue.movies.repositories.AdapterGenresRepository
import com.ru.movieshows.app.glue.movies.repositories.AdapterMoviesRepository
import com.ru.movieshows.movies.domain.repositories.GenresRepository
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MoviesRepositoriesModule {

    @Binds
    fun bindMoviesRepository(moviesRepository: AdapterMoviesRepository): MoviesRepository

    @Binds
    fun bindGenresRepository(genresRepository: AdapterGenresRepository): GenresRepository

}