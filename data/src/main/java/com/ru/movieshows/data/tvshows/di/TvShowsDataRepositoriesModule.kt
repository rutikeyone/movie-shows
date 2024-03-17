package com.ru.movieshows.data.tvshows.di

import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.tvshows.TvShowsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TvShowsDataRepositoriesModule {

    @Binds
    fun provideTvShowsDataRepository(
        tvShowsDataRepository: TvShowsDataRepositoryImpl,
    ): TvShowsDataRepository

}