package com.ru.movieshows.app.presentation.viewmodel.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.ru.movieshows.R
import com.ru.movieshows.app.model.AppFailure
import com.ru.movieshows.app.model.genres.GenresRepository
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.presentation.screens.movies.MoviesFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.movies.state.DiscoverMoviesState
import com.ru.movieshows.app.presentation.viewmodel.movies.state.MoviesState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.genres.entities.GenreEntity
import com.ru.movieshows.sources.movies.entities.MovieEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MoviesViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val moviesRepository: MoviesRepository,
    private val genresRepository: GenresRepository,
): BaseViewModel() {

    private val _moviesState = MutableLiveData<MoviesState>()
    val moviesState = _moviesState.share()

    private val _discoverMoviesState = MutableLiveData<DiscoverMoviesState>()
    val discoverMoviesState = _discoverMoviesState.share()

    private val _tabIndexState = MutableLiveData(0)
    val tabIndexState get() = _tabIndexState.value ?: 0

    init {
        fetchMoviesData()
    }

    fun fetchMoviesData() = viewModelScope.launch {
        languageTagFlow.collect {
            _moviesState.value = MoviesState.InPending
            try {
                val nowPlayingMovies = fetchNowPlayingMovies(it)
                val genres = fetchGenres(it)
                val upcomingMovies = fetchUpcomingMovies(it).second
                val popularMovies = fetchPopularMovies(it).second
                val topRatedMovies = fetchTopRatedMovies(it).second
                _moviesState.value  = MoviesState.Success(nowPlayingMovies, genres, upcomingMovies, popularMovies, topRatedMovies)
                fetchDiscoverMovies()
            } catch (e: AppFailure) {
                _moviesState.value = MoviesState.Failure(e.headerResource(), e.errorResource())
            }
        }
    }

    fun changeTabIndex(value: Int){
        _tabIndexState.value = value
        fetchDiscoverMovies()
    }

    private suspend fun fetchNowPlayingMovies(language: String): ArrayList<MovieEntity> {
        val fetchMoviesNowPlayingResult = moviesRepository.getNowPlayingMovies(page = 1, language = language)
        return fetchMoviesNowPlayingResult.getOrElse {
            throw it
        }
    }

    private suspend fun fetchGenres(language: String): ArrayList<GenreEntity> {
        val fetchGenresResult = genresRepository.getGenres(language)
        return fetchGenresResult.getOrElse {
            throw it
        }
    }

    fun fetchDiscoverMovies() = viewModelScope.launch {
        val currentState = _moviesState.value ?: return@launch
        if(currentState !is MoviesState.Success) return@launch
        val genres = currentState.genres
        if(genres.isEmpty()) return@launch
        _discoverMoviesState.value = DiscoverMoviesState.InPending
        try {
            val genre = genres[tabIndexState]
            val id = genre.id
            if(id == null) {
                _discoverMoviesState.value = DiscoverMoviesState.Failure(R.string.an_error_occurred_during_the_operation)
            }
            val discoverMovies = fetchDiscoverMoviesByGenre(genre)
            _discoverMoviesState.value = DiscoverMoviesState.Success(discoverMovies)
        } catch (e: AppFailure) {
            _discoverMoviesState.value = DiscoverMoviesState.Failure(e.errorResource())
        }
    }

    private suspend fun fetchDiscoverMoviesByGenre(genre: GenreEntity): ArrayList<MovieEntity> {
        val page = 1
        val fetchDiscoverMovies = moviesRepository.getDiscoverMovies(languageTag, page, genre.id!!)
        return fetchDiscoverMovies.getOrElse {
            throw it
        }
    }

    private suspend fun fetchUpcomingMovies(language: String): Pair<Int, ArrayList<MovieEntity>> {
        val page = 1
        val fetchUpcomingMovies = moviesRepository.getUpcomingMovies(language, page)
        return fetchUpcomingMovies.getOrElse {
            throw it
        }
    }

    private suspend fun fetchPopularMovies(language: String): Pair<Int, ArrayList<MovieEntity>> {
        val page = 1
        val fetchPopularMovies = moviesRepository.getPopularMovies(language, page)
        return fetchPopularMovies.getOrElse {
            throw it
        }
    }

    private suspend fun fetchTopRatedMovies(language: String): Pair<Int, ArrayList<MovieEntity>> {
        val page = 1
        val fetchTopRatedMovies = moviesRepository.getTopRatedMovies(language, page)
        return fetchTopRatedMovies.getOrElse {
            throw it
        }
    }

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    fun navigateToUpcomingMovies() {
        val action = MoviesFragmentDirections.actionMoviesFragmentToUpcomingMoviesFragment()
        navigator.navigate(action)
    }

    fun navigateToPopularMovies() {
        val action = MoviesFragmentDirections.actionMoviesFragmentToPopularMoviesFragment2()
        navigator.navigate(action)
    }

    fun navigateToTopRatedMovies() {
        val action = MoviesFragmentDirections.actionMoviesFragmentToTopRatedMoviesFragment()
        navigator.navigate(action)
    }

    fun navigateToMovieSearch() {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieSearchFragment()
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): MoviesViewModel
    }
}