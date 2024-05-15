package com.ru.movieshows.tv_shows.domain

import androidx.paging.PagingData
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import com.ru.movieshows.tv_shows.domain.repositories.TvShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedPopularTvShowsUseCase @Inject constructor(
    private val tvShowsRepository: TvShowsRepository,
) {

    fun execute(
        language: String = "en_US",
    ): Flow<PagingData<TvShow>> {
        return tvShowsRepository.getPagedPopularTvShows(language)
    }

}