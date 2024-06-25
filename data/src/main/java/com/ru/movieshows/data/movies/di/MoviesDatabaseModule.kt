package com.ru.movieshows.data.movies.di

import com.ru.movieshows.data.AppDatabase
import com.ru.movieshows.data.movies.room.MovieSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MoviesDatabaseModule {

    @Provides
    fun provideMovieSearchDao(database: AppDatabase): MovieSearchDao {
        return database.getMovieSearchDao()
    }

}