package com.ru.movieshows.dependencies

import com.ru.movieshows.data.repository.AccountRepository
import com.ru.movieshows.data.repository.AppSettingsRepository
import com.ru.movieshows.data.repository.GenresRepository
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.data.repository.TvShowRepository
import com.ru.movieshows.data.repository.YoutubeRepository
import com.ru.movieshows.domain.repository.AccountRepositoryImpl
import com.ru.movieshows.domain.repository.AppSettingsRepositoryImpl
import com.ru.movieshows.domain.repository.GenresRepositoryImpl
import com.ru.movieshows.domain.repository.MoviesRepositoryImpl
import com.ru.movieshows.domain.repository.TvShowsRepositoryImpl
import com.ru.movieshows.domain.repository.YoutubeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGenresRepository(genresRepositoryImpl: GenresRepositoryImpl): GenresRepository

    @Binds
    abstract fun bindMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun bindTvShowsRepository(tvShowsRepositoryImpl: TvShowsRepositoryImpl): TvShowRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(repository: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindAccountsRepository(repository: AccountRepositoryImpl) : AccountRepository

    @Binds
    @Singleton
    abstract fun bindYoutubeRepository(repository: YoutubeRepositoryImpl): YoutubeRepository

}
