package com.ru.movieshows.presentation.viewmodel.top_rated_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.presentation.screens.popular_movies.PopularMoviesFragmentDirections
import com.ru.movieshows.presentation.screens.top_rated_movies.TopRatedMoviesFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TopRatedMoviesViewModel @Inject constructor(moviesRepository: MoviesRepository) : BaseViewModel() {
    val topRatedMovies: Flow<PagingData<MovieEntity>> = moviesRepository.getPagedTopRatedMovies(currentLanguage)
        .cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity) {
        if(movie.id == null) return
        navigationEvent.publishEvent(NavigationIntent.To(TopRatedMoviesFragmentDirections.actionTopRatedMoviesFragmentToMovieDetailsFragment(movie.id)))
    }
}