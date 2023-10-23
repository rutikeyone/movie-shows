package com.ru.movieshows.presentation.viewmodel.movie_search

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.presentation.screens.movie_search.MovieSearchFragmentDirections
import com.ru.movieshows.presentation.screens.movies.MoviesFragmentDirections
import com.ru.movieshows.presentation.utils.MutableUnitLiveEvent
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.utils.share
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : BaseViewModel(){
    private var _query = MutableLiveData("")
    val query = _query.share()

    val queryValue get() = if(_query.value.isNullOrEmpty()) null else _query.value
    val searchQueryMode: Boolean get() = !_query.value.isNullOrEmpty()

    val searchMovies = _query.switchMap {
        moviesRepository.searchPagedMovies(currentLanguage, queryValue)
        .cachedIn(viewModelScope)
    }

    fun changeQuery(query: String) {
        if(queryValue != query) {
            _query.postValue(query)
        }
    }

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val direction = MovieSearchFragmentDirections.actionMovieSearchFragmentToMovieDetailsFragment(id)
        val intent = NavigationIntent.To(direction)
        navigationEvent.publishEvent(intent)
    }
}
