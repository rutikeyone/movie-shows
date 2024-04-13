package com.ru.movieshows.data.tv_shows.di

import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.tv_shows.TvShowsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TvShowsDataRepositoriesModule {

    @Binds
    fun provideTvShowsDataRepository(
        tvShowsDataRepository: TvShowsDataRepositoryImpl,
    ): TvShowsDataRepository

}