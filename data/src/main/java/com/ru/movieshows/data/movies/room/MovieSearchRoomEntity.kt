package com.ru.movieshows.data.movies.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ru.movieshows.data.movies.models.MovieModel


@Entity(tableName = "movies_search")
data class MovieSearchRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movieId: Int?,
    val name: String?,
    val timeInMilSeconds: Long,
    val locale: String,
) {

    constructor(movie: MovieModel, locale: String) : this(
        movieId = movie.id,
        name = movie.title,
        timeInMilSeconds = System.currentTimeMillis(),
        locale = locale,
    )

}