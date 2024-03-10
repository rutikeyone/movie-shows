package com.ru.movieshows.data.tvshows.di

import com.ru.movieshows.data.TvShowsDataRepository
import com.ru.movieshows.data.tvshows.TvShowsDataRepositoryImpl
import dagger.Binds
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Singleton
@InstallIn(Singleton::class)
interface TvShowsDataRepositoriesModule {

    @Binds
    fun provideTvShowsDataRepository(
        tvShowsDataRepository: TvShowsDataRepositoryImpl,
    ): TvShowsDataRepository

}