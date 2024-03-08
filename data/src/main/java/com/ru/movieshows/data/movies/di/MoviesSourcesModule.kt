package com.ru.movieshows.data.movies.di

import com.ru.movieshows.data.movies.sources.MoviesSource
import com.ru.movieshows.data.movies.sources.MoviesSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MoviesSourcesModule {

    @Binds
    fun bindMoviesSource(
        moviesSource: MoviesSourceImpl,
    ): MoviesSource

}