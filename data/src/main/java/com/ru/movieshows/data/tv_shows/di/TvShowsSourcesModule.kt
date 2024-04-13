package com.ru.movieshows.data.tv_shows.di

import com.ru.movieshows.data.tv_shows.sources.TvShowsSource
import com.ru.movieshows.data.tv_shows.sources.TvShowsSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TvShowsSourcesModule {

    @Binds
    fun bindTvShowsSource(
        tvShowsSource: TvShowsSourceImpl,
    ): TvShowsSource

}