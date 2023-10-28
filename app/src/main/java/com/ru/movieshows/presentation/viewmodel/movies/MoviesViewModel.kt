package com.ru.movieshows.presentation.viewmodel.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ru.movieshows.R
import com.ru.movieshows.data.repository.GenresRepository
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.GenreEntity
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.utils.AppFailure
import com.ru.movieshows.presentation.screens.movies.MoviesFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val genresRepository: GenresRepository,
): BaseViewModel() {
    private val _state = MutableLiveData<MoviesState>()
    val state = _state.share()
    private val currentState get() = state.value

    private val _moviesDiscoverState = MutableLiveData<MoviesDiscoverState>()
    val discoverMoviesState = _moviesDiscoverState.share()

    private val _tabIndex = MutableLiveData(0)
    val tabIndex get() = _tabIndex.value ?: 0

    init {
        fetchMoviesData()
    }


    fun fetchMoviesData() = viewModelScope.launch {
        currentLanguage.collect {
            _state.value = MoviesState.InPending
            try {
                val nowPlayingMovies = fetchNowPlayingMovies(it)
                val genres = fetchGenres(it)
                val upcomingMovies = fetchUpcomingMovies(it)
                val popularMovies = fetchPopularMovies(it)
                val topRatedMovies = fetchTopRatedMovies(it)
                _state.value  = MoviesState.Success(nowPlayingMovies, genres, upcomingMovies, popularMovies, topRatedMovies)
                fetchDiscoverMovies()
            } catch (e: AppFailure) {
                _state.value = MoviesState.Failure(e.headerResource(), e.errorResource())
            }
        }
    }

    fun changeTabIndex(value: Int){
        _tabIndex.value = value
        fetchDiscoverMovies()
    }

    private suspend fun fetchNowPlayingMovies(language: String): ArrayList<MovieEntity> {
        val fetchMoviesNowPlayingResult = moviesRepository.getMoviesNowPlaying(page = 1, language = language)
        return fetchMoviesNowPlayingResult.getOrThrow()
    }

    private suspend fun fetchGenres(language: String): ArrayList<GenreEntity> {
        val fetchGenresResult = genresRepository.getGenres(language)
        return fetchGenresResult.getOrThrow()
    }

    fun fetchDiscoverMovies() = viewModelScope.launch {
        if(currentState !is MoviesState.Success) return@launch
        val genres = (currentState as MoviesState.Success).genres
        _moviesDiscoverState.value = MoviesDiscoverState.InPending
        try {
            val genre = genres[tabIndex]
            if(genre.id == null) {
                _moviesDiscoverState.value = MoviesDiscoverState.Failure(R.string.an_error_occurred_during_the_operation)
            }
            val discoverMovies = fetchDiscoverMoviesByGenre(genre)
            _moviesDiscoverState.value = MoviesDiscoverState.Success(discoverMovies)
        } catch (e: AppFailure) {
            _moviesDiscoverState.value = MoviesDiscoverState.Failure(e.errorResource())
        }

    }

    private suspend fun fetchDiscoverMoviesByGenre(genre: GenreEntity): ArrayList<MovieEntity> {
        val page = 1
        val fetchDiscoverMovies = moviesRepository.getDiscoverMovies(languageTag, page, genre.id!!)
        return fetchDiscoverMovies.getOrThrow()
    }

    private suspend fun fetchUpcomingMovies(language: String): ArrayList<MovieEntity> {
        val page = 1
        val fetchUpcomingMovies = moviesRepository.getUpcomingMovies(language, page)
        return fetchUpcomingMovies.getOrThrow()
    }

    private suspend fun fetchPopularMovies(language: String): ArrayList<MovieEntity> {
        val page = 1
        val fetchPopularMovies = moviesRepository.getPopularMovies(language, page)
        return fetchPopularMovies.getOrThrow()
    }

    private suspend fun fetchTopRatedMovies(language: String): ArrayList<MovieEntity> {
        val page = 1
        val fetchTopRatedMovies = moviesRepository.getTopRatedMovies(language, page)
        return  fetchTopRatedMovies.getOrThrow()
    }

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        val intent = NavigationIntent.To(action)
        navigationEvent.publishEvent(intent)
    }

    fun navigateToUpcomingMovies() {
        val intent = NavigationIntent.To(MoviesFragmentDirections.actionMoviesFragmentToUpcomingMoviesFragment())
        navigationEvent.publishEvent(intent)
    }

    fun navigateToPopularMovies() {
        val intent = NavigationIntent.To(MoviesFragmentDirections.actionMoviesFragmentToPopularMoviesFragment2())
        navigationEvent.publishEvent(intent)
    }

    fun navigateToTopRatedMovies() {
        val intent = NavigationIntent.To(MoviesFragmentDirections.actionMoviesFragmentToTopRatedMoviesFragment())
        navigationEvent.publishEvent(intent)
    }

    fun navigateToMovieSearch() {
        val intent = NavigationIntent.To(MoviesFragmentDirections.actionMoviesFragmentToMovieSearchFragment())
        navigationEvent.publishEvent(intent)
    }
}