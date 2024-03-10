package com.ru.movieshows.data.tvshows.di

import com.ru.movieshows.data.tvshows.api.TvShowsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
class TvShowsApiModule {

    @Provides
    fun provideTvShowsApi(retrofit: Retrofit): TvShowsApi {
        return retrofit.create(TvShowsApi::class.java)
    }

}