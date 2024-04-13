package com.ru.movieshows.movies.presentation.movies

import com.ru.movieshows.core.Container
import com.ru.movieshows.core.presentation.BaseViewModel
import com.ru.movieshows.core.presentation.SimpleAdapterListener
import com.ru.movieshows.movies.MoviesRouter
import com.ru.movieshows.movies.domain.GetDiscoverMoviesUseCase
import com.ru.movieshows.movies.domain.GetGenresUseCase
import com.ru.movieshows.movies.domain.GetNowPlayingMoviesUseCase
import com.ru.movieshows.movies.domain.GetPopularMoviesUseCase
import com.ru.movieshows.movies.domain.GetTopRatedMoviesUseCase
import com.ru.movieshows.movies.domain.GetUpcomingMoviesUseCase
import com.ru.movieshows.movies.domain.entities.Genre
import com.ru.movieshows.movies.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val router: MoviesRouter,
    private val getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : BaseViewModel(), SimpleAdapterListener<Movie> {

    private val loadScreenStateFlow = MutableStateFlow<Container<State>>(Container.Pending)
    private val indexState = MutableStateFlow<Int?>(null)
    private val loadDiscoverMoviesStateFlow =
        MutableStateFlow<Container<List<Movie>>>(Container.Pending)

    val loadScreenStateLiveValue = loadScreenStateFlow
        .toLiveValue(Container.Pending)

    val loadDiscoverMoviesStateValue = loadDiscoverMoviesStateFlow
        .toLiveValue(Container.Pending)

    val indexStateLiveValue = indexState
        .toLiveValue(null)

    init {

        viewModelScope.launch {

            launch {
                languageTagFlow.collect {
                    getMoviesData(it)
                }
            }

            launch {
                indexState
                    .collect { state ->
                        getDiscoverMovies(state)
                    }
            }

        }

    }

    private suspend fun getMoviesData(language: String) {
        val firstPageIndex = 1

        loadScreenStateFlow.value = Container.Pending

        try {
            val nowPlayingMovies = getNowPlayingMoviesUseCase.execute(language)
            val genres = getGenresUseCase.execute(language)
            val upcomingMovies = getUpcomingMoviesUseCase.execute(language, firstPageIndex)
            val popularMovies = getPopularMoviesUseCase.execute(language, firstPageIndex)
            val topRatedMovies = getTopRatedMoviesUseCase.execute(language, firstPageIndex)
            val state = State(
                nowPlayingMovies = nowPlayingMovies,
                genres = genres,
                upcomingMovies = upcomingMovies,
                popularMovies = popularMovies,
                topRatedMovies = topRatedMovies,
            )

            loadScreenStateFlow.value = Container.Success(state)
            indexState.value = 0

        } catch (e: Exception) {
            loadScreenStateFlow.value = Container.Error(e)
        }
    }

    fun tryGetDiscoverMovies() {
        val currentIndex = indexState.value
        viewModelScope.launch {
            getDiscoverMovies(currentIndex)
        }
    }

    fun tryToGetMoviesData() {
        viewModelScope.launch {
            getMoviesData(languageTag)
        }
    }

    private suspend fun getDiscoverMovies(index: Int?) {
        try {
            val index = index ?: return
            val successState = loadScreenStateFlow.value.getOrNull() ?: return
            val genres = successState.genres
            val currentGenre = genres.getOrNull(index)
            val id = currentGenre?.id
            val firstPageIndex = 1

            loadDiscoverMoviesStateFlow.value = Container.Pending

            val discoverMovies = getDiscoverMoviesUseCase.execute(
                language = languageTag,
                page = firstPageIndex,
                withGenresId = id,
            )

            loadDiscoverMoviesStateFlow.value = Container.Success(discoverMovies)

        } catch (e: Exception) {
            loadDiscoverMoviesStateFlow.value = Container.Error(e)
        }
    }

    fun changeTabIndex(value: Int) = debounce {
        indexState.value = value
    }

    override fun onClickItem(movie: Movie) {
        launchMoviesDetails(movie)
    }

    private fun launchMoviesDetails(movie: Movie) = debounce {
        router.launchMovieDetails(movie)
    }

    fun launchUpcomingMovies() = debounce {
        router.launchUpcomingMovies()
    }

    fun launchPopularMovies() = debounce {
        router.launchPopularMovies()
    }

    fun launchTopRatedMovies() = debounce {
        router.launchTopRatedMovies()
    }

    fun launchMovieSearch() = debounce {
        router.launchMovieSearch()
    }

    data class State(
        val nowPlayingMovies: List<Movie>,
        val genres: List<Genre>,
        val upcomingMovies: List<Movie>,
        val popularMovies: List<Movie>,
        val topRatedMovies: List<Movie>,
    )

}