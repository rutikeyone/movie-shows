package com.ru.movieshows.tv_shows.presentation.tv_shows

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.tv_shows.TvShowsRouter
import com.ru.movieshows.tv_shows.domain.GetPopularTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.GetTopRatedTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.GetTrendingTvShowsUseCase
import com.ru.movieshows.tv_shows.domain.entities.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    private val router: TvShowsRouter,
    private val getTrendingTvShowsUseCase: GetTrendingTvShowsUseCase,
    private val getAirTvShowsUseCase: GetTrendingTvShowsUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
) : BaseViewModel(), SimpleAdapterListener<TvShow> {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    init {

        viewModelScope.launch {
            languageTagFlow.collect {
                getTvShowsData(it)
            }
        }

    }

    fun toTryToGetTvShowsData() = debounce {
        viewModelScope.launch {
            getTvShowsData(languageTag)
        }
    }

    private suspend fun getTvShowsData(language: String) {
        val firstPageIndex = 1

        loadScreenStateFlow.value = Container.Pending

        try {
            val trendingTvShowsPagination =
                getTrendingTvShowsUseCase.execute(language, firstPageIndex)
            val airTvShowsPagination = getAirTvShowsUseCase.execute(language, firstPageIndex)
            val topRatedTvShowsPagination =
                getTopRatedTvShowsUseCase.execute(language, firstPageIndex)
            val popularTvShowsPagination =
                getPopularTvShowsUseCase.execute(language, firstPageIndex)

            val trendingTvShows = trendingTvShowsPagination.results
            val airTvShows = airTvShowsPagination.results
            val topRatedTvShows = topRatedTvShowsPagination.results
            val popularTvShows = popularTvShowsPagination.results

            val state = State(
                trendingTvShows = trendingTvShows,
                airTvShows = airTvShows,
                topRatedTvShows = topRatedTvShows,
                popularTvShows = popularTvShows
            )

            loadScreenStateFlow.value = Container.Success(state)
        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }

    }

    fun launchTvShowSearch() = debounce {
        router.launchTvShowSearch()
    }

    override fun onClickItem(data: TvShow) = debounce {
        launchTvShowDetails(data)
    }

    private fun launchTvShowDetails(tvShow: TvShow) = debounce {
        router.launchTvShowsDetails(tvShow)
    }

    fun launchAirTvShows() = debounce {
        router.launchOnTheAirTvShows()
    }

    fun launchTopRatedTvShows() = debounce {
        router.launchTopRatedTvShows()
    }

    fun launchPopularTvShows() = debounce {
        router.launchPopularTvShows()
    }

    data class State(
        val trendingTvShows: List<TvShow>,
        val airTvShows: List<TvShow>,
        val topRatedTvShows: List<TvShow>,
        val popularTvShows: List<TvShow>,
    )

}