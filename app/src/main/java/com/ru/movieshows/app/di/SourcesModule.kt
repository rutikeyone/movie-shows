package com.ru.movieshows.app.di

import com.ru.movieshows.app.model.accounts.AccountsSource
import com.ru.movieshows.app.model.genres.GenresSource
import com.ru.movieshows.app.model.movies.MoviesSource
import com.ru.movieshows.app.model.tv_shows.TvShowsSource
import com.ru.movieshows.sources.accounts.AccountsSourceImpl
import com.ru.movieshows.sources.genres.GenresSourceImpl
import com.ru.movieshows.sources.movies.MoviesSourceImpl
import com.ru.movieshows.sources.tv_shows.TvShowsSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    @Binds
    abstract fun provideAccountSource(accountsSource: AccountsSourceImpl): AccountsSource

    @Binds
    abstract fun bindsGenresSource(genresSource: GenresSourceImpl): GenresSource

    @Binds
    abstract fun bindsMoviesSource(moviesSource: MoviesSourceImpl): MoviesSource

    @Binds
    abstract fun bindsTvShowsSource(tvShowSource: TvShowsSourceImpl): TvShowsSource

}