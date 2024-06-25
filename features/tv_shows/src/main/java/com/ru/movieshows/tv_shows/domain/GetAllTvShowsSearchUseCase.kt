package com.ru.movieshows.tv_shows.domain

import com.ru.movieshows.tv_shows.domain.entities.TvShowSearch
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTvShowsSearchUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    fun execute(locale: String): Flow<List<TvShowSearch>> {
        return tvShowsRepository.getAllTvShowsSearch(locale)
    }

}