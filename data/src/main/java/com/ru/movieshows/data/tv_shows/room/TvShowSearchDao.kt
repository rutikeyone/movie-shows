package com.ru.movieshows.data.tv_shows.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShowsSearch(tvShowSearch: TvShowSearchRoomEntity)

    @Query("DELETE FROM tv_shows_search WHERE id = :id")
    suspend fun deleteTvShowSearch(id: Long)

    @Query("SELECT * FROM tv_shows_search WHERE locale = :locale ORDER BY timeInMilSeconds DESC")
    fun getAllTvShowsSearch(locale: String): Flow<List<TvShowSearchRoomEntity>>

}