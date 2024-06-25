package com.ru.movieshows.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ru.movieshows.data.movies.room.MovieSearchDao
import com.ru.movieshows.data.movies.room.MovieSearchRoomEntity
import com.ru.movieshows.data.tv_shows.room.TvShowSearchDao
import com.ru.movieshows.data.tv_shows.room.TvShowSearchRoomEntity

@Database(
    version = 1,
    entities = [
        MovieSearchRoomEntity::class,
        TvShowSearchRoomEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieSearchDao(): MovieSearchDao

    abstract fun getTvShowsSearchDao(): TvShowSearchDao

}