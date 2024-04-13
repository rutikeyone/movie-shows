package com.ru.movieshows.app.glue.tv_shows.di

import com.ru.movieshows.app.glue.tv_shows.AdapterTvShowsRouter
import com.ru.movieshows.tv_shows.presentation.TvShowsRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TvShowsRouterModule {

    @Binds
    fun bindTvShowsRouter(tvShowsRouter: AdapterTvShowsRouter): TvShowsRouter

}