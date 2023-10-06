package com.ru.movieshows.presentation.viewmodel.upcoming_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.domain.repository.MoviesRepository
import com.ru.movieshows.presentation.screens.uncoming_movies.UpcomingMoviesFragmentDirections
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    moviesRepository: MoviesRepository,
) : BaseViewModel() {
    val upcomingMovies: Flow<PagingData<MovieEntity>> =
        moviesRepository.getPagedUnComingMovies(currentLanguage)
            .cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        if(movie.id == null) return;
        navigationEvent.publishEvent(NavigationIntent.To(UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailsFragment(movie.id)))
    }
}