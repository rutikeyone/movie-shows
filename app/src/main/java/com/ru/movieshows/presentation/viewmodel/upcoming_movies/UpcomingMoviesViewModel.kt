package com.ru.movieshows.presentation.viewmodel.upcoming_movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ru.movieshows.data.repository.MoviesRepository
import com.ru.movieshows.domain.entity.MovieEntity
import com.ru.movieshows.presentation.screens.uncoming_movies.UpcomingMoviesFragmentDirections
import com.ru.movieshows.presentation.sideeffects.navigator.NavigatorWrapper
import com.ru.movieshows.presentation.viewmodel.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class UpcomingMoviesViewModel @AssistedInject constructor(
    @Assisted private val navigator: NavigatorWrapper,
    private val moviesRepository: MoviesRepository,
) : BaseViewModel() {

    val upcomingMovies: Flow<PagingData<MovieEntity>> = languageTagFlow.flatMapLatest {
        moviesRepository.getPagedUnComingMovies(it)
    }.cachedIn(viewModelScope)

    fun navigateToMovieDetails(movie: MovieEntity){
        val id = movie.id ?: return
        val action = UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailsFragment(id)
        navigator.navigate(action)
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: NavigatorWrapper): UpcomingMoviesViewModel
    }

}