package com.ru.movieshows.app.di

import com.ru.movieshows.app.model.accounts.AccountsSource
import com.ru.movieshows.app.model.genres.GenresSource
import com.ru.movieshows.app.model.movies.MoviesSource
import com.ru.movieshows.app.model.people.PeopleSource
import com.ru.movieshows.app.model.tvshows.TvShowsSource
import com.ru.movieshows.sources.accounts.AccountsSourceImpl
import com.ru.movieshows.sources.genres.GenresSourceImpl
import com.ru.movieshows.sources.movies.MoviesSourceImpl
import com.ru.movieshows.sources.people.PeopleSourceImpl
import com.ru.movieshows.sources.tvshows.TvShowsSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SourcesModule {

    @Binds
    fun provideAccountSource(accountsSource: AccountsSourceImpl): AccountsSource

    @Binds
    fun bindsGenresSource(genresSource: GenresSourceImpl): GenresSource

    @Binds
    fun bindsMoviesSource(moviesSource: MoviesSourceImpl): MoviesSource

    @Binds
    fun bindsTvShowsSource(tvShowSource: TvShowsSourceImpl): TvShowsSource

    @Binds
    fun bindsPeopleSource(peopleSource: PeopleSourceImpl): PeopleSource

}