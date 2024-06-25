package com.ru.movieshows.data.tv_shows.di

import com.ru.movieshows.data.AppDatabase
import com.ru.movieshows.data.tv_shows.room.TvShowSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class TvShowsDatabaseModule {

    @Provides
    fun provideTvShowSearchDao(database: AppDatabase): TvShowSearchDao {
        return database.getTvShowsSearchDao()
    }

}