package com.ru.movieshows.presentation.viewmodel.popular_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PopularMoviesViewModel @Inject constructor(moviesRepository: MoviesRepository): BaseViewModel() {

    val popularMovies: Flow<PagingData<MovieEntity>> = languageTagFlow.flatMapLatest {
        moviesRepository.getPagedPopularMovies(it)
    }.cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        //TODO
//        val id = movie.id ?: return
//        val action = NavigationIntent.toMovieDetails(id)
//        navigationEvent.publishEvent(action)
    }
}