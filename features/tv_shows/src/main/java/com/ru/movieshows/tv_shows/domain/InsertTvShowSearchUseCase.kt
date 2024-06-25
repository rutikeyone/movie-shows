package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import javax.inject.Inject

class InsertTvShowSearchUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    suspend fun execute(tvShow: TvShow, locale: String) {
        return tvShowsRepository.insertTvShowSearch(tvShow, locale)
    }

}