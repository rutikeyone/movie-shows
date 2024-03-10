package com.ru.movieshows.data.tvshows.di

import com.ru.movieshows.data.tvshows.sources.TvShowsSource
import com.ru.movieshows.data.tvshows.sources.TvShowsSourceImpl
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