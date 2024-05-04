package com.ru.movieshows.movies.domain

import com.ru.movieshows.movies.domain.entities.Video
import com.ru.movieshows.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetVideosByIdUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
) {

    suspend fun execute(language: String = "en_US", movieId: Int): List<Video> {
        return moviesRepository.getVideosById(
            language = language,
            movieId = movieId,
        )
    }

}