package com.ru.movieshows.app.glue.movies.mappers

import com.ru.movieshows.data.movies.room.MovieSearchRoomEntity
import com.ru.movieshows.movies.domain.entities.MovieSearch
import javax.inject.Inject

class MovieSearchMapper @Inject constructor() {

    fun toMovieSearch(entity: MovieSearchRoomEntity): MovieSearch {
        return MovieSearch(
            id = entity.id,
            movieId = entity.movieId,
            name = entity.name,
            timeInMilSeconds = entity.timeInMilSeconds,
        )
    }

}