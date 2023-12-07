package com.ru.movieshows.presentation.viewmodel.movie_search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


@HiltViewModel
class MovieSearchViewModel @Inject constructor(
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
        setupMediatorState()
    }

    private fun setupMediatorState() {
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
        //TODO
    //        val id = movie.id ?: return
//        val action = NavigationIntent.toMovieDetails(id)
//        navigationEvent.publishEvent(action)
    }
}
