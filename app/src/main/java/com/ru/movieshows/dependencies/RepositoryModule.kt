package com.ru.movieshows.dependencies

import com.ru.movieshows.data.repository.GenresRepositoryImpl
import com.ru.movieshows.data.repository.MoviesRepositoryImpl
import com.ru.movieshows.data.repository.TvShowsRepositoryImpl
import com.ru.movieshows.domain.repository.GenresRepository
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.domain.repository.TvShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindGenresRepository(genresRepositoryImpl: GenresRepositoryImpl): GenresRepository

    @Binds
    abstract fun bindMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun bindTvShowsRepository(tvShowsRepositoryImpl: TvShowsRepositoryImpl): TvShowRepository
}