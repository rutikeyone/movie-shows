package com.ru.movieshows.app.presentation.viewmodel.movies

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.app.model.movies.MoviesRepository
import com.ru.movieshows.app.presentation.adapters.SimpleAdapterListener
import com.ru.movieshows.app.presentation.screens.movies.MovieSearchFragmentDirections
import com.ru.movieshows.app.presentation.sideeffects.navigator.Navigator
import com.ru.movieshows.app.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.app.presentation.viewmodel.movies.state.MovieSearchState
import com.ru.movieshows.app.utils.share
import com.ru.movieshows.sources.movies.entities.MovieEntity
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
) : BaseViewModel(), SimpleAdapterListener<MovieEntity> {

    val state = MediatorLiveData<MovieSearchState>()

    private var _query = MutableLiveData("")
    val query = _query.share()
    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value

    private val _nowPlayingMovies = MutableLiveData<ArrayList<MovieEntity>?>()

    val isSearchMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchMovies = state.asFlow().flatMapLatest {
        moviesRepository.searchPagedMovies(it.language, it.query)
    }.cachedIn(viewModelScope)

    init {
        setupMediatorState()
        collectNowPlayingMovies()
    }

    private fun collectNowPlayingMovies() = viewModelScope.launch {
        languageTagFlow.collect { language ->
            val firstPageIndex = 1
            val fetchMoviesNowPlayingResult = moviesRepository.getNowPlayingMovies(language, firstPageIndex)
            val movies = fetchMoviesNowPlayingResult.getOrNull()
            _nowPlayingMovies.value = movies
        }
    }

    private fun setupMediatorState() {
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

    override fun onClickItem(data: MovieEntity){
        val id = data.id ?: return
        val action = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    override fun onClickPosition(position: Int) {
        val item = state.value?.nowPlayingMovies?.getOrNull(position) ?: return
        onClickItem(item)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator) : MovieSearchViewModel
    }

}
