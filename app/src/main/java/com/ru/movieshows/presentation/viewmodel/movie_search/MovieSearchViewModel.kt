package com.ru.movieshows.presentation.viewmodel.movie_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.movie_search.MovieSearchFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import com.ru.movieshows.presentation.viewmodel.share
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest


@OptIn(ExperimentalCoroutinesApi::class)
class MovieSearchViewModel @AssistedInject constructor(
    @Assisted private val navigator: NavigatorWrapper,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(){
    private val state = MediatorLiveData<Pair<String, String>>()

    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchMovies = state.asFlow().flatMapLatest {
        moviesRepository.searchPagedMovies(it.second, it.first)
    }.cachedIn(viewModelScope)

    init {
        configureMediatorState()
    }

    private fun configureMediatorState() {
        state.addSource(_query) { query ->
            val languageTag = languageTagState.value ?: return@addSource
            state.value = Pair(query, languageTag)
        }

        state.addSource(languageTagState) { languageTag ->
            val query = _query.value ?: return@addSource
            state.value = Pair(query, languageTag)
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
        fun create(navigator: NavigatorWrapper) : MovieSearchViewModel
    }

}
