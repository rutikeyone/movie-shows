package com.ru.movieshows.presentation.viewmodel.popular_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.popular_movies.PopularMoviesFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(moviesRepository: MoviesRepository): BaseViewModel() {

    val popularMovies: Flow<PagingData<MovieEntity>> = currentLanguage.flatMapLatest {
        moviesRepository.getPagedPopularMovies(it)
    }.cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        if(movie.id == null) return;
        val directions = PopularMoviesFragmentDirections
        val action = directions.actionPopularMoviesFragmentToMovieDetailsFragment(movie.id)
        val intent = NavigationIntent.To(action)
        navigationEvent.publishEvent(intent)
    }
}