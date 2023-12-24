package com.ru.movieshows.presentation.viewmodel.movie_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.movie_search.MovieSearchFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.movie_search.state.MovieSearchState
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class MovieSearchViewModel @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(){
    val state = MediatorLiveData<MovieSearchState>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    private val _nowPlayingMovies = MutableLiveData<ArrayList<MovieEntity>?>()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val isSearchMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchMovies = state.asFlow().flatMapLatest {
        moviesRepository.searchPagedMovies(it.language, it.query)
    }.cachedIn(viewModelScope)

    init {
        configureMediatorState()
        collectNowPlayingMovies()
    }

    private fun collectNowPlayingMovies() = viewModelScope.launch {
        languageTagFlow.collect { language ->
            val fetchMoviesNowPlayingResult = moviesRepository.getMoviesNowPlaying(
                page = 1,
                language = language
            )
            val movies = fetchMoviesNowPlayingResult.getOrNull()
            _nowPlayingMovies.value = movies
        }
    }

    private fun configureMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = languageTagState.value ?: return@addSource
            val nowPlayingMovies = _nowPlayingMovies.value
            state.value = MovieSearchState(languageTag, query, nowPlayingMovies, isSearchMode)
        }

        state.addSource(languageTagState) { languageTag ->
            val query = _query.value ?: return@addSource
            val nowPlayingMovies = _nowPlayingMovies.value
            state.value = MovieSearchState(languageTag, query, nowPlayingMovies, isSearchMode)
        }

        state.addSource(_nowPlayingMovies) { nowPlayingMovies ->
            val languageTag = languageTagState.value ?: return@addSource
            val query = _query.value ?: return@addSource
            state.value = MovieSearchState(languageTag, query, nowPlayingMovies, isSearchMode)
        }
    }

    fun changeQuery(query: String) {
        if(queryValue != query) {
            _query.postValue(query)
        }
    }

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator) : MovieSearchViewModel
    }

}
