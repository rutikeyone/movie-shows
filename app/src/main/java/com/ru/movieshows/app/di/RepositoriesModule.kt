package com.ru.movieshows.app.di

import com.ru.movieshows.app.model.accounts.AccountRepository
import com.ru.movieshows.app.model.accounts.AccountsRepositoryImpl
import com.ru.movieshows.app.model.genres.GenresRepository
import com.ru.movieshows.app.model.genres.GenresRepositoryImpl
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.model.movies.MoviesRepositoryImpl
import com.ru.movieshows.app.model.people.PeopleRepository
import com.ru.movieshows.app.model.people.PeopleRepositoryImpl
import com.ru.movieshows.app.model.settings.AppSettingsRepository
import com.ru.movieshows.app.model.settings.SharedPreferencesAppSettingsRepository
import com.ru.movieshows.app.model.tv_shows.TvShowRepository
import com.ru.movieshows.app.model.tv_shows.TvShowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindGenresRepository(genresRepositoryImpl: GenresRepositoryImpl): GenresRepository

    @Binds
    fun bindMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindTvShowsRepository(tvShowsRepositoryImpl: TvShowRepositoryImpl): TvShowRepository

    @Binds
    @Singleton
    fun bindAppSettingsRepository(repository: SharedPreferencesAppSettingsRepository): AppSettingsRepository

    @Binds
    @Singleton
    fun bindAccountsRepository(repository: AccountsRepositoryImpl) : AccountRepository

    @Binds
    fun bindPeopleRepository(repository: PeopleRepositoryImpl): PeopleRepository

}