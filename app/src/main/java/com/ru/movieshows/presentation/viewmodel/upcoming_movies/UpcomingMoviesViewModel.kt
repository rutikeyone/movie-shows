package com.ru.movieshows.presentation.viewmodel.upcoming_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.utils.NavigationIntent
import com.ru.movieshows.presentation.utils.publishEvent
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class UpcomingMoviesViewModel @Inject constructor(
    moviesRepository: MoviesRepository,
) : BaseViewModel() {

     val upcomingMovies: Flow<PagingData<MovieEntity>> = currentLanguage.flatMapLatest {
        moviesRepository.getPagedUnComingMovies(languageTag)
    }.cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = NavigationIntent.toMovieDetails(id)
        navigationEvent.publishEvent(action)
    }
}