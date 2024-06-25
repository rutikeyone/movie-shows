package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class DeleteTvShowSearchUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(id: Long) {
        return tvShowsRepository.deleteTvShowSearch(id)
    }

}