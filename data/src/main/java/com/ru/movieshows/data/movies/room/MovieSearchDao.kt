package com.ru.movieshows.data.movies.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieSearch(moviesSearch: MovieSearchRoomEntity)

    @Query("DELETE FROM movies_search WHERE id = :id")
    suspend fun deleteMovieSearch(id: Long)

    @Query("SELECT * FROM movies_search WHERE locale = :locale ORDER BY timeInMilSeconds DESC")
    fun getAllMoviesSearch(locale: String): Flow<List<MovieSearchRoomEntity>>

}