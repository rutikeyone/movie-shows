package com.ru.movieshows.app.glue.tv_shows.di

import com.ru.movieshows.app.glue.tv_shows.repositories.AdapterTvShowsRepository
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TvShowsRepositoriesModule {

    @Binds
    fun bindTvShowsRepository(tvShowsRepository: AdapterTvShowsRepository): TvShowsRepository

}