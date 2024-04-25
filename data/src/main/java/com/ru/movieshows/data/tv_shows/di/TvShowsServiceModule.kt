package com.ru.movieshows.data.tv_shows.di

import com.ru.movieshows.data.tv_shows.service.TvShowsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class TvShowsServiceModule {

    @Provides
    fun provideTvShowsService(retrofit: Retrofit): TvShowsService {
        return retrofit.create(TvShowsService::class.java)
    }

}